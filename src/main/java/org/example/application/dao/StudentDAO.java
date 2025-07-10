package org.example.application.dao;

import org.example.application.api.Student;
import org.example.application.repository.StudentRepository;
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
        String sql="SELECT pin, first_name, last_name, time_created FROM public.students;";
        return jdbcHelper.query(sql,STUDENT_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<Student> findById(String studentPin) {
        String sql = "SELECT pin, first_name, last_name, time_created FROM public.students WHERE pin = ?;";
        return jdbcHelper.querySingle(sql, STUDENT_RESULT_SET_MAPPER, studentPin);
    }

    /// TODO: should handle the transaction for updates on this layer!!!!

    @Override
    public void update(String studentPin,Student student) {
        String sql="UPDATE public.students SET first_name=?, last_name=? WHERE pin=?;";
        jdbcHelper.update(sql, student.getFirstName(),student.getLastName(), studentPin);
    }

    @Override
    public Student create(Student student) {
        String sql="INSERT INTO public.students(first_name, last_name, time_created) VALUES ( ?, ?, COALESCE(?, CURRENT_TIMESTAMP));";
        return jdbcHelper.insert(sql,
                STUDENT_RESULT_SET_MAPPER,
                student.getFirstName(),
                student.getLastName(),
                student.getTimeCreated());
    }

    @Override
    public void delete(String studentPin) {
        String sql = "DELETE FROM public.students WHERE pin=?;";
        jdbcHelper.update(sql,studentPin);
    }

    @Override
    public boolean existsById(String studentPin) {
        String sql="SELECT pin, first_name, last_name, time_created FROM public.students WHERE pin = ?;";
        return jdbcHelper.exists(sql, studentPin);
    }
}
