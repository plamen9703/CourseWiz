CREATE OR REPLACE FUNCTION users.get_user_profile_flat(
    in_user_id INT DEFAULT NULL,
    in_username VARCHAR DEFAULT NULL,
    in_email VARCHAR DEFAULT NULL
)
RETURNS TABLE (
    user_id INT,
    username VARCHAR,
    email VARCHAR,
    created_at TIMESTAMP,
    role_names VARCHAR[],
    permission_names VARCHAR[],
    profile_type TEXT,
    profile_first_name VARCHAR,
    profile_last_name VARCHAR
)
AS $$
BEGIN
    IF (in_user_id IS NOT NULL)::INT +
       (in_username IS NOT NULL)::INT +
       (in_email IS NOT NULL)::INT != 1 THEN
        RAISE EXCEPTION 'Exactly one of in_user_id, in_username, or in_email must be provided';
    END IF;
    RETURN QUERY
    SELECT
        u.id,
        u.username,
        u.email,
        u.created_at,

        -- Roles
        ARRAY(
            SELECT r.name
            FROM users.user_roles ur
            JOIN users.roles r ON r.id = ur.role_id
            WHERE ur.user_id = u.id
        ),

        -- Permissions
        ARRAY(
            SELECT p.name
            FROM users.user_permissions up
            JOIN users.permissions p ON p.id = up.permission_id
            WHERE up.user_id = u.id
        ),

        -- Profile info
        CASE
            WHEN up.student_pin IS NOT NULL THEN 'student'
            WHEN up.instructor_id IS NOT NULL THEN 'instructor'
            ELSE NULL
        END,

        COALESCE(s.first_name, i.first_name),
        COALESCE(s.last_name, i.last_name)

    FROM users.users u
    LEFT JOIN users.user_profile up ON up.user_id = u.id
    LEFT JOIN coursera.students s ON s.pin = up.student_pin
    LEFT JOIN coursera.instructors i ON i.id = up.instructor_id
    WHERE
        (in_user_id IS NOT NULL AND u.id = in_user_id)
        OR (in_username IS NOT NULL AND u.username = in_username)
        OR (in_email IS NOT NULL AND u.email = in_email)
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;
