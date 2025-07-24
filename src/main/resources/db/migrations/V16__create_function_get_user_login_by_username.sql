CREATE OR REPLACE FUNCTION users.get_user_login_by_username(
	in_user_username character varying)
    RETURNS TABLE(user_id integer, user_username character varying, user_email character varying, user_password character varying, user_roles character varying[], user_permissions character varying[])
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
	IF in_user_username IS NULL THEN
		RAISE EXCEPTION 'user provided username can not be null or empty!';
	END IF;
	RETURN QUERY
		SELECT
    u.id ,
    u.username ,
    u.email ,
	u.password,
    COALESCE(r.roles, ARRAY[]::VARCHAR[]),
    COALESCE(rp_perms || up_perms, ARRAY[]::VARCHAR[])
FROM users.users u
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
WHERE (in_user_username IS NOT NULL AND in_user_username = u.username);
END;
$BODY$;