CREATE OR REPLACE PROCEDURE users.create_user(
    IN in_username VARCHAR,
    IN in_email VARCHAR,
    IN in_password VARCHAR,
    IN in_role_names VARCHAR[],
    IN in_permissions VARCHAR[] DEFAULT NULL,
    IN type VARCHAR DEFAULT NULL,
    IN in_first_name VARCHAR DEFAULT NULL,
    IN in_last_name VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_user_id INT;
    new_student_pin VARCHAR;
    new_instructor_id INT;
    missing_roles INT;
BEGIN
    SELECT array_length(in_role_names, 1) - COUNT(*) INTO missing_roles
    FROM users.roles
    WHERE name = ANY(in_role_names);

    IF missing_roles > 0 THEN
        RAISE EXCEPTION 'One or more roles do not exist: %', in_role_names;
    END IF;

    INSERT INTO users.users (username, email, password, created_at)
    VALUES (in_username, in_email, in_password, now())
    RETURNING id INTO new_user_id;

    INSERT INTO users.user_roles(user_id, role_id)
    SELECT new_user_id, r.id
    FROM users.roles r
    WHERE r.name = ANY(in_role_names);

    IF in_permissions IS NOT NULL AND array_length(in_permissions, 1) > 0 THEN
        INSERT INTO users.user_permissions(user_id, permission_id)
        SELECT new_user_id, p.id
        FROM users.permissions p
        WHERE p.name = ANY(in_permissions);
    END IF;

    IF type IS NOT NULL THEN
        IF type = 'student' THEN
            INSERT INTO coursera.students(first_name, last_name)
            VALUES (in_first_name, in_last_name)
            RETURNING pin INTO new_student_pin;

            INSERT INTO users.user_profile(user_id, student_pin)
            VALUES (new_user_id, new_student_pin);

        ELSIF type = 'instructor' THEN
            INSERT INTO coursera.instructors(first_name, last_name)
            VALUES (in_first_name, in_last_name)
            RETURNING id INTO new_instructor_id;

            INSERT INTO users.user_profile(user_id, instructor_id)
            VALUES (new_user_id, new_instructor_id);

        ELSE
            RAISE EXCEPTION 'Invalid type: %, expected student or instructor', type;
        END IF;
    END IF;
END;
$$;
