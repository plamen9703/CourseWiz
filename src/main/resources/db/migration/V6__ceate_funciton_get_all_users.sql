CREATE OR REPLACE FUNCTION users.get_all_user_profiles_flat()
RETURNS TABLE (
    user_id INT,
    username VARCHAR,
    email VARCHAR,
    created_at TIMESTAMP,
    role_names VARCHAR[],
    permission_names VARCHAR[],
    profile_type VARCHAR,
    profile_first_name VARCHAR,
    profile_last_name VARCHAR
)
LANGUAGE plpgsql
STABLE
PARALLEL SAFE
AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.id,
        u.username,
        u.email,
        u.created_at,

        -- Roles from LATERAL join
        COALESCE(ur.roles, ARRAY[]::VARCHAR[]),

        -- Permissions from LATERAL join
        COALESCE(upm.permissions, ARRAY[]::VARCHAR[]),

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

    -- Aggregate roles using LATERAL
    LEFT JOIN LATERAL (
        SELECT array_agg(DISTINCT r.name) AS roles
        FROM users.user_roles ur2
        JOIN users.roles r ON r.id = ur2.role_id
        WHERE ur2.user_id = u.id
    ) ur ON TRUE

    -- Aggregate permissions using LATERAL
    LEFT JOIN LATERAL (
        SELECT array_agg(DISTINCT p.name) AS permissions
        FROM users.user_permissions up2
        JOIN users.permissions p ON p.id = up2.permission_id
        WHERE up2.user_id = u.id
    ) upm ON TRUE;
END;
$$;
