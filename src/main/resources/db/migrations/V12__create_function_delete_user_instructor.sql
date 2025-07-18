CREATE OR REPLACE FUNCTION users.delete_user_instructor(
	in_user_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    v_total INTEGER := 0;
    v_count INTEGER;
    instructor_id INTEGER;
BEGIN
    -- Get instructor_id for this user
    SELECT up.instructor_id
    INTO instructor_id
    FROM user_profile up
    WHERE up.user_id = in_user_id;

    -- Delete from instructors
    DELETE FROM coursera.instructors i WHERE i.id = instructor_id;
    GET DIAGNOSTICS v_count = ROW_COUNT;
    v_total := v_total + v_count;

    -- Delete from users (cascade deletes user_profile)
    DELETE FROM users.users u WHERE u.id = in_user_id;
    GET DIAGNOSTICS v_count = ROW_COUNT;
    v_total := v_total + v_count;

    RETURN v_total;
END;
$BODY$;