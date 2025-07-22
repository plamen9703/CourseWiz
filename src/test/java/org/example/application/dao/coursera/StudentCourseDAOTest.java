package org.example.application.dao.coursera;

import org.example.application.api.coursera.StudentCourse;
import org.example.db.JdbcHelperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class StudentCourseDAOTest {

    private final JdbcHelperImpl jdbcHelper = mock(JdbcHelperImpl.class);

    private StudentCourseDAO studentCourseDAO;

    @BeforeEach
    void setUp() {
        studentCourseDAO=new StudentCourseDAO(jdbcHelper);
    }

    @AfterEach
    void tearDown() {
        reset(jdbcHelper);
    }

    @Test
    void findAll() {
        String sql = "SELECT student_pin, course_id, completion_date FROM coursera.student_course_xref;";
        StudentCourse studentCourse=new StudentCourse();
        when(jdbcHelper.query(anyString(), any())).thenReturn(List.of(studentCourse));
        studentCourseDAO.findAll();
        verify(jdbcHelper)
                .query(
                        eq(sql),
                        eq(StudentCourseDAO.STUDENT_COURSE_RESULT_SET_MAPPER)
                );
    }

    @Test
    void findById() {
        String sql = "SELECT student_pin, course_id, completion_date FROM coursera.student_course_xref WHERE student_pin=? AND course_id=?;";
        StudentCourse studentCourse=new StudentCourse();
        studentCourse.setCourseId(2);
        studentCourse.setStudentPin("000");
        when(jdbcHelper.query(anyString(), any())).thenReturn(List.of(studentCourse));
        studentCourseDAO.findById(studentCourse);
        verify(jdbcHelper)
                .query(
                        eq(sql),
                        eq(StudentCourseDAO.STUDENT_COURSE_RESULT_SET_MAPPER),
                        eq("000"),
                        eq(2)
                );
    }

    @Test
    void insert() {
        String sql = "INSERT INTO coursera.student_course_xref(student_pin, instructor_id, completion_date) VALUES (?, ?, ?)";
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void existsById() {
    }
}