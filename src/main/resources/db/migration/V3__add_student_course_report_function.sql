
CREATE OR REPLACE FUNCTION get_student_course_report(
	p_student_pins character varying[],
	p_min_credit integer,
	p_start_date date,
	p_end_date date)
    RETURNS TABLE("Student PIN" character varying, "Student Name" character varying, "Total Credit" integer, "Course ID" integer, "Course Name" character varying, "Total Time" smallint, "Credit" smallint, "Instructor Name" character varying)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $$
BEGIN
    RETURN QUERY
    WITH report_data AS (
        SELECT
            s.pin,
            CONCAT(s.first_name, ' ', s.last_name)::VARCHAR AS student_name,
            c.id AS course_id,
            c.name::VARCHAR AS course_name,
            c.total_time,
            c.credit,
            CONCAT(i.first_name, ' ', i.last_name)::VARCHAR AS instructor_name,
            sc.completion_date
        FROM students s
        JOIN student_course_xref sc ON s.pin = sc.student_pin
        JOIN courses c ON c.id = sc.course_id
        JOIN instructors i ON c.instructor_id = i.id
       WHERE sc.completion_date IS NOT NULL AND
    		(p_student_pins IS NULL OR s.pin = ANY(p_student_pins)) AND
    		(p_start_date IS NULL OR p_end_date IS NULL OR sc.completion_date BETWEEN p_start_date AND p_end_date)

    ),
    total_credits AS (
        SELECT
            pin,
            SUM(credit)::INT AS total_credit
        FROM report_data
        GROUP BY pin
    )
    SELECT
        rd.pin AS "Student PIN",
        rd.student_name AS "Student Name",
        tc.total_credit AS "Total Credit",
        rd.course_id AS "Course ID",
        rd.course_name AS "Course Name",
        rd.total_time AS "Total Time",
        rd.credit AS "Credit",
        rd.instructor_name AS "Instructor Name"
    FROM report_data rd
    JOIN total_credits tc ON rd.pin = tc.pin
    WHERE (p_min_credit IS NULL OR tc.total_credit >= p_min_credit)
    ORDER BY tc.total_credit DESC, rd.student_name ASC;
END;
$$;


