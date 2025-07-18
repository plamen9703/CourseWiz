package org.example.application.dao.coursera;

import org.example.application.api.coursera.Student;
import org.example.application.repository.coursera.StudentRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.List;
import java.util.Optional;

public class StudentDAO  implements StudentRepository {

    private final JdbcHelper jdbcHelper;

    private static final ResultSetMapper<Student> STUDENT_RESULT_SET_MAPPER= rs->new Student(
            rs.getString("pin"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getTimestamp("time_created")
    );

    public StudentDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }

    @Override
    public List<Student> findAll() {
        String sql="SELECT pin, first_name, last_name, time_created FROM coursera.students;";
        return jdbcHelper.query(sql,STUDENT_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<Student> findById(Student student) {
        String sql = "SELECT pin, first_name, last_name, time_created FROM coursera.students WHERE pin = ?;";
        return jdbcHelper.querySingle(sql, STUDENT_RESULT_SET_MAPPER, student.getPin());
    }


    @Override
    public int update(Student student) {
        String sql="UPDATE coursera.students SET first_name=?, last_name=? WHERE pin=?;";
        return jdbcHelper.update(sql, student.getFirstName(),student.getLastName(), student.getPin());
    }

    @Override
    public Student insert(Student student) {
        String sql="INSERT INTO coursera.students(first_name, last_name, time_created) VALUES ( ?, ?, COALESCE(?, CURRENT_TIMESTAMP));";
        return jdbcHelper.insert(sql,
                STUDENT_RESULT_SET_MAPPER,
                student.getFirstName(),
                student.getLastName(),
                student.getTimeCreated());
    }

    @Override
    public int delete(Student student) {
        String sql = "DELETE FROM coursera.students WHERE pin=?;";
        return jdbcHelper.update(sql,student.getPin());
    }

    @Override
    public boolean existsById(Student student) {
        String sql="SELECT pin, first_name, last_name, time_created FROM coursera.students WHERE pin = ?;";
        return jdbcHelper.exists(sql, student.getPin());
    }
}
