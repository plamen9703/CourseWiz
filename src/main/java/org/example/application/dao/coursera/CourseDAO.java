package org.example.application.dao.coursera;

import org.example.application.api.coursera.Course;
import org.example.application.repository.coursera.CourseRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.List;
import java.util.Optional;


public class CourseDAO implements CourseRepository {

    private final JdbcHelper jdbcHelper;

    public static final ResultSetMapper<Course> COURSE_RESULT_SET_MAPPER = rs -> new Course(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("instructor_id"),
            rs.getShort("total_time"),
            rs.getShort("credit"),
            rs.getTimestamp("time_created")
    );

    public CourseDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper=jdbcHelper;
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT id, name, instructor_id, total_time, credit, time_created FROM coursera.courses;";
        return jdbcHelper.query(sql, COURSE_RESULT_SET_MAPPER);
    }


    @Override
    public Optional<Course> findById(Course course) {
        String sql="SELECT id, name, instructor_id, total_time, credit, time_created FROM coursera.courses WHERE id=?;";
        return jdbcHelper.querySingle(sql,COURSE_RESULT_SET_MAPPER, course.getId());
    }

    @Override
    public Course insert(Course course) {
        String sql = """
        INSERT INTO coursera.courses(name, instructor_id, total_time, credit) VALUES (?, ?, ?, ?)
        RETURNING id, name, instructor_id, total_time, credit, time_created;""";
        return jdbcHelper.insert(sql,
                COURSE_RESULT_SET_MAPPER,
                course.getName(),
                course.getInstructorId(),
                course.getTotalTime(),
                course.getCredit());
    }

    @Override
    public int update(Course course) {
        String sql = "UPDATE coursera.courses SET name=?, instructor_id=?, total_time=?, credit=? WHERE id=?;";
        return jdbcHelper.update(sql,
                course.getName(),
                course.getInstructorId(),
                course.getTotalTime(),
                course.getCredit(),
                course.getId());
    }

    @Override
    public int delete(Course course) {
        String sql = "DELETE FROM coursera.courses WHERE id=?;";
        return jdbcHelper.update(sql, course.getId());
    }

    @Override
    public boolean existsById(Course course) {
        String sql="SELECT id, name, instructor_id, total_time, credit, time_created FROM coursera.courses WHERE id=?;";
        return jdbcHelper.exists(sql, course.getId());
    }
}