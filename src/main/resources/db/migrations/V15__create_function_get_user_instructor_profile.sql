CREATE OR REPLACE FUNCTION users.get_user_instructor_profile_by_identifier(
	in_user_id integer,
	in_user_username character varying,
	in_user_email character varying)
    RETURNS TABLE(user_id integer, user_username character varying, user_email character varying, user_roles character varying[], user_permissions character varying[], instructor_id integer, instructor_first_name character varying, instructor_last_name character varying, instructor_created_at timestamp without time zone)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
	IF
		(
		(CASE WHEN in_user_id IS NOT NULL THEN 1 ELSE 0 END) +
		(CASE WHEN in_user_username IS NOT NULL THEN 1 ELSE 0 END)+
		(CASE WHEN in_user_email IS NOT NULL THEN 1 ELSE 0 END)
		)!=1 THEN
			RAISE EXCEPTION 'Exactly one identifier must be provided!';
	END IF;
	RETURN QUERY
		SELECT
    u.id ,
    u.username ,
    u.email ,
    COALESCE(r.roles, ARRAY[]::VARCHAR[]),
    COALESCE(rp_perms || up_perms, ARRAY[]::VARCHAR[]),
    i.id,
    i.first_name ,
    i.last_name,
    i.time_created
FROM users.users u
JOIN users.user_profile up ON up.user_id = u.id
JOIN coursera.instructors i ON up.instructor_id = i.id
LEFT JOIN LATERAL (
    SELECT ARRAY_AGG(r.name) AS roles
    FROM users.roles r
    JOIN users.user_roles ur ON ur.role_id = r.id
    WHERE ur.user_id = u.id
) r ON TRUE
LEFT JOIN LATERAL (
    SELECT ARRAY_AGG(p.name) AS rp_perms
    FROM users.roles r
    JOIN users.role_permissions rp ON rp.role_id = r.id
    JOIN users.permissions p ON p.id = rp.permission_id
    WHERE r.id IN (
        SELECT role_id FROM users.user_roles ur WHERE ur.user_id = u.id
    )
) rp2 ON TRUE
LEFT JOIN LATERAL (
    SELECT ARRAY_AGG(p.name) AS up_perms
    FROM users.user_permissions up2
    JOIN users.permissions p ON up2.permission_id = p.id
    WHERE up2.user_id = u.id
) up2 ON TRUE
WHERE
(in_user_id IS NOT NULL AND in_user_id = u.id)
OR (in_user_username IS NOT NULL AND in_user_username = u.username)
OR(in_user_email IS NOT NULL AND in_user_email = u.email);
END;
$BODY$;