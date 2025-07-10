package org.example.application.dao;

import org.example.application.api.StudentCourse;
import org.example.application.repository.StudentCourseRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.List;
import java.util.Optional;

public class StudentCourseDAO  implements StudentCourseRepository {
    private final JdbcHelper jdbcHelper;

    private static final ResultSetMapper<StudentCourse> STUDENT_COURSE_RESULT_SET_MAPPER= rs ->new StudentCourse(
            rs.getString("student_pin"),
            rs.getInt("course_id"),
            rs.getDate("completion_date")
    );

    public StudentCourseDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper=jdbcHelper;
    }

    @Override
    public List<StudentCourse> findAll() {
        String sql="SELECT student_pin, course_id, completion_date FROM public.student_course_xref;";
        return jdbcHelper.query(sql, STUDENT_COURSE_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<StudentCourse> findById(String studentPin, Integer courseId) {
        String sql="SELECT student_pin, course_id, completion_date FROM public.student_course_xref WHERE student_pin=? AND course_id=?;";
        return jdbcHelper.querySingle(sql,STUDENT_COURSE_RESULT_SET_MAPPER, studentPin, courseId);
    }

    @Override
    public StudentCourse create(StudentCourse studentCourse) {
        String sql = "INSERT INTO public.student_course_xref(student_pin, course_id, completion_date) VALUES (?, ?, ?);";
        return jdbcHelper.insert(sql,
                STUDENT_COURSE_RESULT_SET_MAPPER,
                studentCourse.getStudentPin(),
                studentCourse.getCourseId(),
                studentCourse.getCompletionDate());
    }

    @Override
    public void update(String studentPin, Integer courseId,StudentCourse studentCourse) {
        String sql="UPDATE public.student_course_xref SET completion_date=? WHERE student_pin=? AND course_id=?;";
        jdbcHelper.update(sql, studentCourse.getCompletionDate(),studentPin, courseId);
    }

    @Override
    public void delete(String studentPin, Integer courseId) {
        String sql = "DELETE FROM public.student_course_xref WHERE student_pin=? AND course_id=?;";
        jdbcHelper.update(sql,studentPin, courseId);
    }

    @Override
    public boolean existById(String studentPin, Integer courseId) {
        String sql = "SELECT student_pin, course_id, completion_date FROM public.student_course_xref WHERE student_pin=?, course_id=?;";
        return jdbcHelper.exists(sql, studentPin, courseId);
    }
}
