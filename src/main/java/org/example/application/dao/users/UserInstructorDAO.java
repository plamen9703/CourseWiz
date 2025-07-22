package org.example.application.dao.users;

import org.example.application.api.coursera.Instructor;
import org.example.application.api.users.UserInstructor;
import org.example.application.repository.users.UserInstructorRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import javax.ws.rs.NotFoundException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserInstructorDAO extends UserDAO<UserInstructor> implements UserInstructorRepository {

    private final JdbcHelper jdbcHelper;

    public UserInstructorDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper=jdbcHelper;
    }
    private  class UserMapperWithPassword implements ResultSetMapper<UserInstructor> {

        @Override
        public UserInstructor map(ResultSet rs) throws SQLException {
            UserInstructor user = new UserInstructor();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("user_username"));
            user.setEmail(rs.getString("user_email"));
            user.setPassword(rs.getString("user_password"));
            user.setRoles(new HashSet<>(List.of((String[])rs.getArray("user_roles").getArray())));
            user.setPermissions(new HashSet<>(List.of((String[])  rs.getArray("user_permissions").getArray())));
//            Array roleArray = rs.getArray("role_names");
//            if (roleArray != null) {
//                String[] perms = (String[]) roleArray.getArray();
//                user.setRoles(new HashSet<>(Arrays.asList(perms)));
//            }
            return  user;
        }
    }
    private final UserMapperWithPassword USER_MAPPER_WITH_PASSWORD=new UserMapperWithPassword();
    private static final ResultSetMapper<UserInstructor> USER_INSTRUCTOR_RESULT_SET_MAPPER =
            rs -> {
                UserInstructor userInstructor=new UserInstructor();
                userInstructor.setId(rs.getInt("user_id"));
                userInstructor.setUsername(rs.getString("user_username"));
                userInstructor.setEmail(rs.getString("user_email"));

                Array roleArray = rs.getArray("user_roles");
                String[] roleStringArray = (String[]) roleArray.getArray();
                userInstructor.setRoles(new HashSet<>(List.of(roleStringArray)));

                Array permissionsArray = rs.getArray("user_permissions");
                String[] permissionsStringArray = (String[]) permissionsArray.getArray();
                userInstructor.setPermissions(new HashSet<>(List.of(permissionsStringArray)));

                Instructor instructor = new Instructor();
                instructor.setId(rs.getInt("instructor_id"));
                instructor.setFirstName(rs.getString("instructor_first_name"));
                instructor.setLastName(rs.getString("instructor_last_name"));
                instructor.setTimeCreated(rs.getTimestamp("instructor_created_at"));

                userInstructor.setInstructor(instructor);
                return userInstructor;
            };


    @Override
    public List<UserInstructor> findAll() {
        String sql = "SELECT * FROM users.get_user_instructor_profile_all();";
        return jdbcHelper.query(sql,USER_INSTRUCTOR_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<UserInstructor> findById(UserInstructor userInstructor) {
        String sql="SELECT * FROM users.get_user_instructor_profile_by_identifier(?,null, null);";
        return jdbcHelper.querySingle(sql, USER_INSTRUCTOR_RESULT_SET_MAPPER, userInstructor.getId());
    }


    @Override
    public UserInstructor insert(UserInstructor userInstructor) {
        String sql = """
                SELECT create_user_instructor AS "user_id" FROM users.create_user_instructor(?,?,?,?,?);""";
        UserInstructor created = jdbcHelper.insert(
                sql,
                rs -> {
                    UserInstructor newUserInstructor = new UserInstructor();
                    newUserInstructor.setId(rs.getInt("user_id"));
                    return newUserInstructor;
                },
                userInstructor.getUsername(),
                userInstructor.getEmail(),
                userInstructor.getPassword(),
                userInstructor.getInstructor().getFirstName(),
                userInstructor.getInstructor().getLastName());
        return findById(created).orElseThrow(()->new NotFoundException(String.format("User with ID: %d not found.",created.getId())));
    }

    @Override
    public int update(UserInstructor userInstructor) {
        String sql = """
        UPDATE users.users u
        SET
            username = COALESCE(NULLIF($1,?), u.username),
            email = COALESCE(NULLIF($1,?), u.email),
            password = COALESCE(NULLIF($1,?), u.password)
        WHERE id = ?;""";

        return jdbcHelper.update(sql,
                userInstructor.getUsername(),
                userInstructor.getEmail(),
                userInstructor.getPassword(),
                userInstructor.getId());
    }

    @Override
    public int delete(UserInstructor userInstructor) {
        String sql = "SELECT * FROM delete_user_instructor(?)";
        return jdbcHelper.update(sql, userInstructor.getId());
    }

    @Override
    public boolean existsById(UserInstructor userInstructor) {
        String sql ="SELECT u.id, u.username, u.email FROM users.users u WHERE u.id = ?";
        return jdbcHelper.exists(sql, userInstructor.getId());
    }

    @Override
    public Optional<UserInstructor> findByUsername(UserInstructor userInstructor) {
        return Optional.empty();
    }

    @Override
    public Optional<UserInstructor> findByEmail(UserInstructor userInstructor) {
        String sql = """
                SELECT * FROM users.get_user_instructor_login(null,?);
                """;
        return jdbcHelper.querySingle(sql, USER_MAPPER_WITH_PASSWORD, userInstructor.getUsername());
    }

    @Override
    public boolean existsByUsername(UserInstructor user) {
        String sql = "SELECT u.username FROM users.users u WHERE u.username = ?;";
        return jdbcHelper.exists(sql, user.getUsername());
    }

    @Override
    public boolean existsByEmail(UserInstructor userInstructor) {
        String sql = "SELECT u.email FROM users.users u WHERE u.email = ?;";
        return jdbcHelper.exists(sql, userInstructor.getEmail());
    }
}
