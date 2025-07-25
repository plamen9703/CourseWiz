package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.StudentCourse;
import org.example.application.dao.coursera.CourseDAO;
import org.example.application.dao.coursera.StudentCourseDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentCourseServiceImplTest {

    private final StudentCourseDAO studentCourseDAO=mock(StudentCourseDAO.class);
    private StudentCourseServiceImpl studentCourseService;
    private final CourseDAO courseDAO = mock(CourseDAO.class);


    @BeforeEach
    void setUp() {
        studentCourseService=new StudentCourseServiceImpl(studentCourseDAO, courseDAO);
    }

    @AfterEach
    void tearDown() {
        reset(studentCourseDAO);
    }

    @Test
    void findAll() {
        StudentCourse studentCourse=new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        when(studentCourseDAO.findAll()).thenReturn(List.of(studentCourse));
        List<StudentCourse> studentCourses=studentCourseService.findAll();
        assertEquals(1, studentCourses.size());
        assertEquals(studentCourse,studentCourses.get(0));
    }

    @Test
    void findByIdWhenOptionalIsEmpty() {
        when(studentCourseDAO.findById(any(StudentCourse.class))).thenReturn(Optional.empty());
        StudentCourse studentCourse=new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentCourseService.findById(studentCourse));
        assertEquals("Student with PIN: 000 is not enrolled in course with ID: 1",exception.getMessage());
    }

    @Test
    void findByIdWhenOptionalIsNotEmpty() {
        StudentCourse studentCourse=new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        when(studentCourseDAO.findById(any(StudentCourse.class))).thenReturn(Optional.of(studentCourse));
        StudentCourse found = studentCourseService.findById(studentCourse);
        assertEquals(studentCourse,found);
    }

    @Test
    void createWhenStudentIsAlreadyEnrolledInCourse() {
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentCourseService.create(studentCourse));
        assertEquals("Student already enrolled in course! Student: 000, Course: 1",exception.getMessage());
        verify(studentCourseDAO, never()).insert(any(StudentCourse.class));
    }

    @Test
    void createWhenStudentIsIsNotInCourse() {
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(false);
        when(studentCourseDAO.insert(studentCourse)).thenReturn(studentCourse);
        StudentCourse created = studentCourseService.create(studentCourse);
        assertEquals(studentCourse,created);
        verify(studentCourseDAO, times(1)).existsById(studentCourse);
        verify(studentCourseDAO, times(1)).insert(studentCourse);
    }

    @Test
    void updateWhenStudentIsNotEnrolledInCourse() {
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(false);
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentCourseService.update(studentCourse));
        assertEquals("Student with PIN: 000 is not enrolled in course with ID: 1",exception.getMessage());
        verify(studentCourseDAO,never()).update(any(StudentCourse.class));
    }

    @Test
    void updateWhenStudentIsEnrolledInCourse() {
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(true);
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        studentCourseService.update(studentCourse);
        verify(studentCourseDAO,times(1)).update(any(StudentCourse.class));
    }

    @Test
    void deleteWhenStudentIsNotEnrolledInCourse() {
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(false);
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentCourseService.delete(studentCourse));
        assertEquals("Student with PIN: 000 is not enrolled in course with ID: 1",exception.getMessage());
        verify(studentCourseDAO,never()).delete(any(StudentCourse.class));
    }

    @Test
    void deleteWhenStudentIsEnrolledInCourse() {
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(true);
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        studentCourseService.delete(studentCourse);
        verify(studentCourseDAO,times(1)).delete(any(StudentCourse.class));
    }

    @Test
    void existsByIdWhenStudentIsNotEnrolledInCourse() {
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(false);
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        assertFalse(studentCourseService.existsById(studentCourse));
    }

    @Test
    void existsByIdWhenStudentIsEnrolledInCourse() {
        when(studentCourseDAO.existsById(any(StudentCourse.class))).thenReturn(true);
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin("000");
        studentCourse.setCourseId(1);
        assertTrue(studentCourseService.existsById(studentCourse));
    }
}