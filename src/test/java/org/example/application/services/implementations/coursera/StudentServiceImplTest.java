package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Student;
import org.example.application.dao.coursera.StudentDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    private final StudentDAO studentDAO = mock(StudentDAO.class);

    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(studentDAO);
    }

    @AfterEach
    void tearDown() {
        reset(studentDAO);
    }

    @Test
    void findByIdWhenOptionalIsEmpty() {
        Student student = new Student();
        when(studentDAO.findById(any(Student.class))).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.findById(student));
        assertEquals("Student not found with PIN: null", exception.getMessage());
        verify(studentDAO,times(1)).findById(any(Student.class));
    }

    @Test
    void findByIdWhenOptionalIsNotEmpty() {
        Student student = new Student();
        student.setFirstName("Plamen");
        when(studentDAO.findById(any(Student.class))).thenReturn(Optional.of(student));
        Student found = studentService.findById(student);
        assertEquals(student, found);
        assertEquals("Plamen", found.getFirstName());
        verify(studentDAO,times(1)).findById(any(Student.class));
    }

    @Test
    void findAll() {
        Student st = new Student("000", "plamen", "hristov", null);
        when(studentDAO.findAll()).thenReturn(List.of(st));
        List<Student> students = studentService.findAll();
        assertEquals(1, students.size());
        assertEquals(st, students.get(0));

    }

    @Test
    void createWhenStudentPinExists() {
        Student student = new Student();
        student.setPin("000");
        when(studentDAO.existsById(any(Student.class))).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.create(student);
        });
        assertEquals("Student with PIN: 000 already exists", exception.getMessage());
        verify(studentDAO,never()).insert(any(Student.class));
    }

    @Test
    void createWhenStudentPinDoesNotExist() {
        Student student = new Student();
        student.setPin("000");
        when(studentDAO.existsById(any(Student.class))).thenReturn(false);
        Student returned = new Student("001", "Ivan", "Ivanov", null);
        when(studentDAO.insert(student)).thenReturn(returned);
        Student created = studentService.create(student);
        assertEquals(returned,created);
        verify(studentDAO,times(1)).insert(any(Student.class));
    }
    @Test
    void updateWhenStudentDoesNotExist() {
        when(studentDAO.existsById(any(Student.class))).thenReturn(false);
        Student student=new Student();
        student.setPin("000");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.update(student));
        assertEquals("Student not found with PIN: 000",exception.getMessage());
        verify(studentDAO,never()).update(any(Student.class));
    }

    @Test
    void updateWhenStudentExists() {
        when(studentDAO.existsById(any(Student.class))).thenReturn(true);
        Student student=new Student();
        student.setPin("000");
        studentService.update(student);
        verify(studentDAO,times(1)).update(any(Student.class));
    }

    @Test
    void deleteWhenStudentDoesNotExist() {
        when(studentDAO.existsById(any(Student.class))).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.delete(mock(Student.class)));
        assertEquals("Student not found with PIN: null", exception.getMessage());
        verify(studentDAO, never()).delete(any(Student.class));
    }

    @Test
    void deleteWhenStudentExists() {
        when(studentDAO.existsById(any(Student.class))).thenReturn(true);
        studentService.delete(mock(Student.class));
        verify(studentDAO, times(1)).delete(any(Student.class));
    }

    @Test
    void existsByIdWhenStudenDoesNotExist() {
        when(studentDAO.existsById(any(Student.class))).thenReturn(false);
        assertFalse(studentService.existsById(mock(Student.class)));
    }

    @Test
    void existsByIdWhenStudenExists() {
        when(studentDAO.existsById(any(Student.class))).thenReturn(true);
        assertTrue(studentService.existsById(mock(Student.class)));
    }
}