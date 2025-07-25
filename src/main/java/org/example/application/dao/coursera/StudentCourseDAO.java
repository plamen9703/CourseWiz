package org.example.application.dao.coursera;

import org.example.application.api.coursera.StudentCourse;
import org.example.application.api.coursera.StudentCourseStatus;
import org.example.application.repository.coursera.StudentCourseRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class StudentCourseDAO  implements StudentCourseRepository {
    private final JdbcHelper jdbc;

    public static final ResultSetMapper<StudentCourse> STUDENT_COURSE_RESULT_SET_MAPPER= rs ->new StudentCourse(
            rs.getString("student_pin"),
            rs.getInt("course_id"),
            rs.getDate("completion_date")
    );

    public StudentCourseDAO(JdbcHelper jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<StudentCourse> findAll() {
        String sql="SELECT student_pin, course_id, completion_date FROM coursera.student_course_xref;";
        return jdbc.query(sql,
                STUDENT_COURSE_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<StudentCourse> findById(StudentCourse studentCourse) {
        String sql="SELECT student_pin, course_id, completion_date FROM coursera.student_course_xref WHERE student_pin=? AND course_id=?;";
        return jdbc.querySingle(sql,
                STUDENT_COURSE_RESULT_SET_MAPPER,
                studentCourse.getStudentPin(),
                studentCourse.getCourseId());
    }

    @Override
    public StudentCourse insert(StudentCourse studentCourse) {
        String sql = """
                INSERT INTO coursera.student_course_xref(student_pin, course_id, completion_date) VALUES (?, ?, ?)
                RETURNING student_pin, course_id, completion_date;""";
        return jdbc.insert(sql,
                STUDENT_COURSE_RESULT_SET_MAPPER,
                studentCourse.getStudentPin(),
                studentCourse.getCourseId(),
                studentCourse.getCompletionDate());
    }

    @Override
    public int update(StudentCourse studentCourse) {
        String sql="UPDATE coursera.student_course_xref SET completion_date=? WHERE student_pin=? AND course_id=?;";
        return jdbc.update(sql,
                studentCourse.getCompletionDate(),
                studentCourse.getStudentPin(),
                studentCourse.getCourseId());
    }

    @Override
    public int delete(StudentCourse studentCourse) {
        String sql = "DELETE FROM coursera.student_course_xref WHERE student_pin=? AND course_id=?;";
        return jdbc.update(sql,
                studentCourse.getStudentPin(),
                studentCourse.getCourseId());
    }

    @Override
    public boolean existsById(StudentCourse studentCourse) {
        String sql = "SELECT student_pin, course_id, completion_date FROM coursera.student_course_xref WHERE student_pin=?, course_id=?;";
        return jdbc.exists(sql,
                studentCourse.getStudentPin(),
                studentCourse.getCourseId());
    }

    @Override
    public List<StudentCourseStatus> getEnrolledStudents(Integer courseId) {
        String sql = """
                SELECT s.pin, s.first_name, s.last_name, sc.completion_date
                FROM coursera.students s
                JOIN coursera.student_course_xref sc ON s.pin=sc.student_pin
                WHERE course_id = ?""";
        List<StudentCourseStatus> students = jdbc.query(sql,
                rs -> {
                    String pin = rs.getString("pin");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    Date compleionDate = rs.getDate("completion_date");
                    return new StudentCourseStatus(pin, courseId, firstName, lastName, compleionDate);
                },
                courseId);
        return students;
    }
}
