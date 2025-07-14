CREATE OR REPLACE PROCEDURE users.create_user(
    in_username VARCHAR,
    in_email VARCHAR,
    in_password VARCHAR,
    in_role_names VARCHAR[],  -- Array of role names
    in_permissions VARCHAR[] DEFAULT NULL,
    type VARCHAR DEFAULT NULL,
    in_first_name VARCHAR DEFAULT NULL,
    in_last_name VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_user_id INT;
    new_student_pin VARCHAR;
    new_instructor_id INT;
BEGIN
        -- Insert user (assuming password is already hashed)
        INSERT INTO users.users (username, email, password, created_at)
        VALUES (in_username, in_email, in_password, now())
        RETURNING id INTO new_user_id;

        -- Insert user roles (loop through in_role_names)
        INSERT INTO users.user_roles(user_id, role_id)
        SELECT new_user_id, r.id
        FROM users.roles r
        WHERE r.name = ANY(in_role_names);

        -- Check that all roles were found
        IF (SELECT COUNT(*) FROM users.roles WHERE name = ANY(in_role_names)) != array_length(in_role_names, 1) THEN
            RAISE EXCEPTION 'One or more roles do not exist: %', in_role_names;
        END IF;

        -- Insert user permissions if provided
        IF in_permissions IS NOT NULL THEN
            INSERT INTO users.user_permissions(user_id, permission_id)
            SELECT new_user_id, p.id
            FROM users.permissions p
            WHERE p.name = ANY(in_permissions);
        END IF;

        -- Create profile based on type
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
