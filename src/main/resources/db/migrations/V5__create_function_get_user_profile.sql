CREATE OR REPLACE FUNCTION users.get_user_profile_flat(
    in_user_id INTEGER DEFAULT NULL,
    in_username VARCHAR DEFAULT NULL,
    in_email VARCHAR DEFAULT NULL
)
RETURNS TABLE(
    user_id INTEGER,
    username VARCHAR,
    email VARCHAR,
    created_at TIMESTAMP,
    role_names VARCHAR[],
    permission_names VARCHAR[],
    profile_type TEXT,
    profile_first_name VARCHAR,
    profile_last_name VARCHAR
)
LANGUAGE plpgsql
STABLE
PARALLEL SAFE
AS $$
BEGIN
    -- Ensure exactly one identifier is provided
    IF (
        (CASE WHEN in_user_id IS NOT NULL THEN 1 ELSE 0 END) +
        (CASE WHEN in_username IS NOT NULL THEN 1 ELSE 0 END) +
        (CASE WHEN in_email IS NOT NULL THEN 1 ELSE 0 END)
    ) != 1 THEN
        RAISE EXCEPTION 'Exactly one of in_user_id, in_username, or in_email must be provided';
    END IF;

    RETURN QUERY
    SELECT
        u.id,
        u.username,
        u.email,
        u.created_at,

        -- Roles
        COALESCE(ur.roles, ARRAY[]::VARCHAR[]),

        -- Permissions
        COALESCE(upm.permissions, ARRAY[]::VARCHAR[]),

        -- Profile type
        CASE
            WHEN up.student_pin IS NOT NULL THEN 'student'
            WHEN up.instructor_id IS NOT NULL THEN 'instructor'
            ELSE NULL
        END AS profile_type,

        COALESCE(s.first_name, i.first_name),
        COALESCE(s.last_name, i.last_name)
    FROM users.users u
    LEFT JOIN users.user_profile up ON up.user_id = u.id
    LEFT JOIN coursera.students s ON s.pin = up.student_pin
    LEFT JOIN coursera.instructors i ON i.id = up.instructor_id

    -- Aggregate roles via LATERAL
    LEFT JOIN LATERAL (
        SELECT array_agg(r.name) AS roles
        FROM users.user_roles ur2
        JOIN users.roles r ON r.id = ur2.role_id
        WHERE ur2.user_id = u.id
    ) ur ON TRUE

    -- Aggregate permissions via LATERAL
    LEFT JOIN LATERAL (
        SELECT array_agg(p.name) AS permissions
        FROM users.user_permissions up2
        JOIN users.permissions p ON p.id = up2.permission_id
        WHERE up2.user_id = u.id
    ) upm ON TRUE

    WHERE
        (in_user_id IS NOT NULL AND u.id = in_user_id)
        OR (in_username IS NOT NULL AND u.username = in_username)
        OR (in_email IS NOT NULL AND u.email = in_email)
    LIMIT 1;
END;
$$;
