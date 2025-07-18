CREATE OR REPLACE FUNCTION users.create_user_student(
	in_username character varying,
	in_email character varying,
	in_password character varying,
	in_first_name character varying DEFAULT NULL::character varying,
	in_last_name character varying DEFAULT NULL::character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    new_user_id INT;
    new_student_pin VARCHAR;
    new_instructor_id INT;
    missing_roles INT;
BEGIN


    INSERT INTO users.users (username, email, password, created_at)
    VALUES (in_username, in_email, in_password, now())
    RETURNING id INTO new_user_id;

    INSERT INTO users.user_roles(user_id, role_id)
    SELECT new_user_id, r.id
    FROM users.roles r
    WHERE r.name = ANY(ARRAY['student']);


    INSERT INTO users.user_permissions(user_id, permission_id)
    SELECT new_user_id, p.id
    FROM users.permissions p
    WHERE p.name = ANY(ARRAY['view-own']);



	INSERT INTO coursera.students(first_name, last_name)
    VALUES (in_first_name, in_last_name)
    RETURNING pin INTO new_student_pin;

    INSERT INTO users.user_profile(user_id, student_pin)
    VALUES (new_user_id, new_student_pin);


	RETURN new_user_id;
END;
$BODY$;