package org.example.application.dao;

import org.example.application.api.Course;
import org.example.application.repository.CourseRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.List;
import java.util.Optional;


public class CourseDAO implements CourseRepository {

    private final JdbcHelper jdbcHelper;

    private static final ResultSetMapper<Course> COURSE_RESULT_SET_MAPPER = rs -> new Course(
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
        String sql = "SELECT id, name, instructor_id, total_time, credit, time_created FROM public.courses;";
        return jdbcHelper.query(sql, COURSE_RESULT_SET_MAPPER);
    }


    @Override
    public Optional<Course> findById(int courseId) {
        String sql="SELECT id, name, instructor_id, total_time, credit, time_created FROM public.courses WHERE id=?;";
        return jdbcHelper.querySingle(sql,COURSE_RESULT_SET_MAPPER, courseId);
    }

    @Override
    public Course create(Course course) {
        String sql = "INSERT INTO public.courses(name, instructor_id, total_time, credit, time_created) VALUES (?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP));";
        return jdbcHelper.insert(sql, COURSE_RESULT_SET_MAPPER,
                course.getName(),
                course.getInstructorId(),
                course.getTotalTime(),
                course.getCredit(),
                course.getTimeCreated());
    }

    @Override
    public int update(Integer courseId,Course course) {
        String sql = "UPDATE public.courses SET  name=?, instructor_id=?, total_time=?, credit=? WHERE id=?";
        return jdbcHelper.update(sql,
                course.getName(),
                course.getInstructorId(),
                course.getTotalTime(),
                course.getCredit(),
                courseId);
    }

    @Override
    public int delete(Integer courseId) {
        String sql = "DELETE FROM public.courses WHERE id=?;";
        return jdbcHelper.update(sql, courseId);
    }

    @Override
    public boolean existsById(int courseId) {
        String sql="SELECT id, name, instructor_id, total_time, credit, time_created FROM public.courses WHERE id=?;";
        return jdbcHelper.exists(sql, courseId);
    }
}
