package org.example.application.dao.users;

import org.example.application.api.coursera.Student;
import org.example.application.api.users.UserStudent;
import org.example.application.repository.users.UserStudentRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import javax.ws.rs.NotFoundException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserStudentDAO implements UserStudentRepository {

    private  class UserMapperWithPassword implements ResultSetMapper<UserStudent> {

        @Override
        public UserStudent map(ResultSet rs) throws SQLException {
            UserStudent user = new UserStudent();
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

    private static class UserStudentMapper implements ResultSetMapper<UserStudent>{

        @Override
        public UserStudent map(ResultSet rs) throws SQLException {
            UserStudent user = new UserStudent();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("user_username"));
            user.setEmail(rs.getString("user_email"));

            // Map roles
            Array roleArray = rs.getArray("user_roles");
            if (roleArray != null) {
                String[] perms = (String[]) roleArray.getArray();
                user.setRoles(new HashSet<>(Arrays.asList(perms)));
            }

            //map permissions
            Array permArray = rs.getArray("user_permissions");
            if (permArray != null) {
                String[] perms = (String[]) permArray.getArray();
                user.setPermissions(new HashSet<>(Arrays.asList(perms)));
            }



            // Map profile
            Student student = new Student();
            student.setPin(rs.getString("student_pin"));
            student.setFirstName(rs.getString("student_first_name"));
            student.setLastName(rs.getString("student_last_name"));
            student.setTimeCreated(rs.getTimestamp("student_created_at"));
            user.setStudent(student);
            return user;
        }

    }
    private static final UserStudentMapper USER_STUDENT_MAPPER = new UserStudentMapper();

    private final JdbcHelper jdbcHelper;
    public UserStudentDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }
    @Override
    public Optional<UserStudent> findByUsername(UserStudent userStudent) {
//                "SELECT * FROM get_user_profile_flat(null,?,null)";
//        SELECT u.id,u.username, u.email,u.password,u.created_at,ARRAY(
//                SELECT r.name
//                FROM users.user_roles ur
//                JOIN users.roles r ON r.id = ur.role_id
//                WHERE ur.user_id = u.id
//        ) AS "role_names"
//        FROM users.users u
//        WHERE username = ?;
        String sql = """
                SELECT * FROM users.get_user_student_login_by_username(?);""";
        return jdbcHelper.querySingle(sql, USER_MAPPER_WITH_PASSWORD, userStudent.getEmail());

    }

    @Override
    public Optional<UserStudent> findByEmail(UserStudent userStudent) {
        String sql = """
                SELECT * FROM users.get_user_student_login_by_email(?);""";
        return jdbcHelper.querySingle(sql, USER_MAPPER_WITH_PASSWORD, userStudent.getEmail());
    }

    @Override
    public boolean existsByUsername(UserStudent userStudent) {
        return findByUsername(userStudent).isPresent();
    }

    @Override
    public boolean existsByEmail(UserStudent userStudent) {
        return findByEmail(userStudent).isPresent();
    }


    @Override
    public List<UserStudent> findAll() {
        String sql = "SELECT users.get_user_student_profile_all()";
        return jdbcHelper.query(sql, USER_STUDENT_MAPPER);
    }



    @Override
    public Optional<UserStudent> findById(UserStudent user) {
        String sql = """
                    SELECT * FROM users.get_user_student_profile_by_identifier(?,null,null)""";
        return jdbcHelper.querySingle(sql, USER_STUDENT_MAPPER,user.getId());
    }

    @Override
    public UserStudent insert(UserStudent userStudent) {
//        String sql = "CALL users.create_user(?,?,?,?,?,?,?,?)";
//        try(Connection conn = jdbcHelper.getConnection();
//            CallableStatement callableStatement = conn.prepareCall(sql)){
//            callableStatement.setString(1, userStudent.getUsername());
//            callableStatement.setString(2, userStudent.getEmail());
//            callableStatement.setString(3, userStudent.getPassword());
//
//            Array rolesArray = conn.createArrayOf("VARCHAR", userStudent.getRoles().toArray());
//            callableStatement.setArray(4, rolesArray);
//
//            if (userStudent.getPermissions() !=null){
//                Array permissionsArray = conn.createArrayOf("VARCHAR", userStudent.getPermissions().toArray());
//                callableStatement.setArray(5, permissionsArray);
//            }else {
//                callableStatement.setNull(5, Types.ARRAY);
//            }
//            String profileType="student";
//            callableStatement.setString(6, profileType);
//            callableStatement.setString(7, userStudent.getStudent().getFirstName());
//            callableStatement.setString(8,userStudent.getStudent().getLastName());
//            callableStatement.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return findByEmail(userStudent).get();
        String sql = """
                SELECT create_user_student AS "user_student_id" FROM users.create_user_student(?,?,?,?,?);""";
        UserStudent creatdUserStudent = jdbcHelper.insert(sql,
                rs -> {
                    UserStudent created = new UserStudent();
                    created.setId(rs.getInt("user_student_id"));
                    System.out.println(created.getId());
                    return created;
                },
                userStudent.getUsername(),
                userStudent.getEmail(),
                userStudent.getPassword(),
                userStudent.getStudent().getFirstName(),
                userStudent.getStudent().getLastName());
        return findById(creatdUserStudent)
                .orElseThrow( () -> new NotFoundException(String.format("User with ID: %d not found.", creatdUserStudent.getId())));
    }




    @Override
    public int update(UserStudent userStudent) {
        String sql="""
        UPDATE users.users u
        SET
            username = COALESCE(NULLIF($1,?), u.username),
            email = COALESCE(NULLIF($1,?), u.email),
            password = COALESCE(NULLIF($1,?), u.password)
        WHERE id = ?;""";
        return jdbcHelper.update(sql,
                userStudent.getPassword(),
                userStudent.getUsername(),
                userStudent.getEmail(),
                userStudent.getId());
    }

    @Override
    public int delete(UserStudent userStudent) {
        String sql = "SELECT * FROM users.delete_user_instructor(?)";
        return jdbcHelper.update(sql, userStudent.getId());
    }

    @Override
    public boolean existsById(UserStudent userStudent) {
        return findById(userStudent).isPresent();
    }
}
