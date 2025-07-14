package org.example.application.dao;

import org.example.application.api.Instructor;
import org.example.application.api.Profile;
import org.example.application.api.Student;
import org.example.application.api.User;
import org.example.application.repository.UserRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserDAO implements UserRepository {


    private final JdbcHelper jdbcHelper;

    private static class UserMapperWithPassword implements ResultSetMapper<User>{

        @Override
        public User map(ResultSet rs) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setCreatedAt(rs.getTimestamp("created_at"));

            Array roleArray = rs.getArray("role_names");
            if (roleArray != null) {
                String[] perms = (String[]) roleArray.getArray();
                user.setRoles(new HashSet<>(Arrays.asList(perms)));
            }
            return user;
        }
    }


    private static class UserMapper implements ResultSetMapper<User>{

        @Override
        public User map(ResultSet rs) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setCreatedAt(rs.getTimestamp("created_at"));

            // Map roles
            Array roleArray = rs.getArray("role_names");
            if (roleArray != null) {
                String[] perms = (String[]) roleArray.getArray();
                user.setRoles(new HashSet<>(Arrays.asList(perms)));
            }

            //map permissions
            Array permArray = rs.getArray("permission_names");
            if (permArray != null) {
                String[] perms = (String[]) permArray.getArray();
                user.setPermissions(new HashSet<>(Arrays.asList(perms)));
            }



            // Map profile
            String profileType = rs.getString("profile_type");
            String firstName = rs.getString("profile_first_name");
            String lastName = rs.getString("profile_last_name");

            if (profileType != null && firstName != null && lastName != null) {
                Profile profile = profileType.equals("student")? (new Student()) : (new Instructor());
                profile.setFirstName(firstName);
                profile.setLastName(lastName);
                user.setProfile(profile);
            }
            return user;
        }

    }
    private static final UserMapper USER_MAPPER = new UserMapper();
    private static final UserMapperWithPassword USER_MAPPER_WITH_PASSWORD=new UserMapperWithPassword();
    public UserDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }
    @Override
    public Optional<User> findByUsername(User user) {
//                "SELECT * FROM get_user_profile_flat(null,?,null)";
        String sql = """
                SELECT u.id,u.username, u.email,u.password,u.created_at,ARRAY(
                            SELECT r.name
                            FROM users.user_roles ur
                            JOIN users.roles r ON r.id = ur.role_id
                            WHERE ur.user_id = u.id
                        ) AS "role_names"
                FROM users.users u
                WHERE username = ?;
                """;
        return jdbcHelper.querySingle(sql, USER_MAPPER_WITH_PASSWORD, user.getUsername());
    }

    @Override
    public Optional<User> findByEmail(User user) {
        String sql = "SELECT * FROM users.get_user_profile_flat(null,null,?)";
        return jdbcHelper.querySingle(sql, USER_MAPPER, user.getEmail());
    }

    @Override
    public boolean existsByUsername(User user) {
        return findByEmail(user).isPresent();
    }

    @Override
    public boolean existsByEmail(User user) {
        return findByEmail(user).isPresent();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users.get_all_user_profiles_flat()";
        return jdbcHelper.query(sql, USER_MAPPER);
    }

    @Override
    public Optional<User> findById(User user) {
        String sql = "SELECT * FROM users.get_user_profile_flat(?,null,null)";
        return jdbcHelper.querySingle(sql, USER_MAPPER,user.getId());
    }

    @Override
    public User insert(User user) {
        String sql = "CALL users.create_user(?,?,?,?,?,?,?,?)";
        try(Connection conn = jdbcHelper.getConnection();
            CallableStatement callableStatement = conn.prepareCall(sql)){
            callableStatement.setString(1, user.getUsername());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getPassword());

            Array rolesArray = conn.createArrayOf("VARCHAR", user.getRoles().toArray());
            callableStatement.setArray(4, rolesArray);

            if (user.getPermissions() !=null){
                Array permissionsArray = conn.createArrayOf("VARCHAR", user.getPermissions().toArray());
                callableStatement.setArray(5, permissionsArray);
            }else {
                callableStatement.setNull(5, Types.ARRAY);
            }
            String profileType=null;
            String profileFirstName=null;
            String profileLastName=null;


            if (user.getProfile() !=null) {
                if (user.getProfile() instanceof Student) {
                    profileType = "student";
                } else if (user.getProfile() instanceof Instructor) {
                    profileType = "instructor";
                }
                profileFirstName = user.getProfile().getFirstName();
                profileLastName = user.getProfile().getLastName();
            }
            callableStatement.setString(6, profileType);
            callableStatement.setString(7, profileFirstName);
            callableStatement.setString(8,profileLastName);
            callableStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findByEmail(user).get();
    }

    @Override
    public int update(User user) {
        String sql="UPDATE users.users SET password = ?, username = ?, email = ? WHERE id=?";
        return jdbcHelper.update(sql,
                user.getPassword(),
                user.getUsername(),
                user.getEmail(),
                user.getId());
    }

    @Override
    public int delete(User user) {
        String sql = "DELETE FROM users.users WHERE id = ?";
        return jdbcHelper.update(sql, user.getId());
    }

    @Override
    public boolean existsById(User user) {
        return findById(user).isPresent();
    }
}
