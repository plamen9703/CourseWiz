package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Course;
import org.example.application.api.coursera.Instructor;
import org.example.application.dao.coursera.CourseDAO;
import org.example.application.dao.coursera.InstructorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    private CourseDAO courseDAO = mock(CourseDAO.class);
    private InstructorDAO instructorDAO = mock(InstructorDAO.class);

    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseServiceImpl(courseDAO, instructorDAO);
    }

    @AfterEach
    void tearDown() {
        reset(courseDAO, instructorDAO);
    }

    @Test
    void findAll() {

        when(courseDAO.findAll()).thenReturn(List.of(
                new Course(1, "Java 101", 1, (short) 5, (short) 50, null)
        ));
        List<Course> courses = courseService.findAll();
        assertEquals(1, courses.size());
        assertEquals("Java 101", courses.get(0).getName());
        verify(courseDAO).findAll();
    }

    @Test
    void findByIdWhenCourseExists() {
        Course courseId1 = new Course();
        courseId1.setId(1);
        Course result = new Course();
        result.setId(1);
        result.setName("Java 101");
        result.setInstructorId(1);
        result.setCredit((short) 6);
        result.setTotalTime((short) 60);
        result.setTimeCreated(Timestamp.valueOf("2025-07-21 10:30:05"));
        when(courseDAO.findById(courseId1)).thenReturn(Optional.of(result));
        Course byId1 = courseService.findById(courseId1);
        assertNotNull(byId1);
        assertEquals("Java 101", byId1.getName());
        verify(courseDAO).findById(courseId1);
    }

    @Test
    void findByIdWhenCourseDoesNotExist() {
        Course courseId2 = new Course();
        courseId2.setId(2);
        when(courseDAO.findById(courseId2)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> courseService.findById(courseId2)
        );
        assertTrue(exception.getMessage().contains("Course with ID: 2 not found."));
        verify(courseDAO).findById(courseId2);
    }

    @Test
    void findByIdWhenIdIsLessThanOrEqualToZero() {
        Course course = new Course();
        course.setId(-1);
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> courseService.findById(course));
        course.setId(0);
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> courseService.findById(course));
        assertEquals("Course with ID: -1 not found.", exception1.getMessage());
        assertEquals("Course with ID: 0 not found.", exception2.getMessage());
        verify(courseDAO, never()).findById(any(Course.class));
    }

    @Test
    void findByIdWhenIdIsNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.findById(new Course()));

        assertEquals("Course with ID: null not found.", exception.getMessage());

        verify(courseDAO, never()).findById(any(Course.class));
    }


    @Test
    void createWhenInstructorExists() {
        Course courseTest = new Course();
        courseTest.setName("Java 101");
        courseTest.setCredit((short) 5);
        courseTest.setTotalTime((short) 5);
        courseTest.setInstructorId(1);

        Instructor instructorTest = new Instructor();
        instructorTest.setId(1);

        Course courseReturned = new Course();
        courseReturned.setId(1);
        courseReturned.setName("Java 101");
        courseReturned.setInstructorId(1);
        courseReturned.setCredit((short) 5);
        courseReturned.setTotalTime((short) 50);
        when(instructorDAO.existsById(instructorTest)).thenReturn(true);
        when(courseDAO.insert(courseTest)).thenReturn(courseReturned);
        Course courseResult = courseService.create(courseTest);

        assertEquals("Java 101", courseResult.getName());
        assertEquals(1, courseResult.getId());
        assertEquals((short) 5, courseResult.getCredit());
        assertEquals((short) 50, courseResult.getTotalTime());
        verify(courseDAO).insert(courseTest);
    }

    @Test
    void createWhenInstructorDoesNotExist() {
        Course courseTest = new Course();
        courseTest.setInstructorId(1);

        Instructor instructorTest = new Instructor();
        instructorTest.setId(1);

        when(instructorDAO.existsById(instructorTest)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.create(courseTest));

        assertEquals("Instructor with ID: 1 not found.", exception.getMessage());
        verify(courseDAO, never()).insert(any(Course.class));
    }

    @Test
    void updateWhenCourseDoesNotExist() {
        Course courseTest = new Course();
        courseTest.setId(1);
        when(courseDAO.findById(courseTest)).thenThrow(new NotFoundException("Course with ID: 1 not found."));
        verify(courseDAO, never()).update(any(Course.class));
    }

    @Test
    void updateWhenNewInstructorIdDoesNotExist() {
        Course courseTest = new Course();
        courseTest.setId(1);
        courseTest.setInstructorId(1);

        Instructor instructorTest = new Instructor();
        instructorTest.setId(1);
        when(courseDAO.findById(courseTest)).thenReturn(Optional.of(courseTest));
        when(instructorDAO.existsById(instructorTest)).thenReturn(false);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.update(courseTest));
        assertEquals("Instructor with ID: 1 not found.", exception.getMessage());
        verify(courseDAO, never()).update(any(Course.class));
    }

    @Test
    void updateWhenNewInstructorIdExists() {
        Course courseTest = new Course();
        courseTest.setId(1);
        courseTest.setInstructorId(1);

        Instructor instructorTest = new Instructor();
        instructorTest.setId(1);
        when(courseDAO.findById(courseTest)).thenReturn(Optional.of(courseTest));
        when(instructorDAO.existsById(instructorTest)).thenReturn(true);
        courseService.update(courseTest);

    }

    @Test
    void updateWhenNewInstructorIdIsNull() {
        Course courseTest = spy(new Course());
        courseTest.setId(1);
        Course courseReturned = new Course();
        courseReturned.setInstructorId(1);

        when(courseDAO.findById(courseTest)).thenReturn(Optional.of(courseReturned));
        courseService.update(courseTest);
        verify(courseTest).setInstructorId(1);
        verify(courseDAO).update(courseTest);

    }

    @Test
    void deleteWhenCourseDoesNotExist() {
        Course courseTest=new Course();
        courseTest.setId(1);

        when(courseDAO.existsById(courseTest)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.delete(courseTest));
        assertEquals("Course with ID: 1 not found.", exception.getMessage());
        verify(courseDAO, never()).delete(any(Course.class));
    }

    @Test
    void deleteWhenCourseExists() {
        Course courseTest=new Course();
        courseTest.setId(1);

        when(courseDAO.existsById(courseTest)).thenReturn(true);
        courseService.delete(courseTest);
        verify(courseDAO, times(1)).delete(courseTest);
    }

    @Test
    void existsByIdWhenIdIsValid() {
        Course course=new Course();
        course.setId(1);
        when(courseDAO.existsById(course)).thenReturn(true);
        assertTrue(courseService.existsById(course));
    }

    @Test
    void existsByIdWhenIdIsNotValid() {
        Course course=new Course();
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> courseService.delete(course));
        course.setId(-1);
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> courseService.delete(course));
        course.setId(0);
        RuntimeException exception3 = assertThrows(RuntimeException.class, () -> courseService.delete(course));

        assertEquals("Course with ID: null not found.", exception1.getMessage());
        assertEquals("Course with ID: -1 not found.", exception2.getMessage());
        assertEquals("Course with ID: 0 not found.", exception3.getMessage());
        verify(courseDAO,never()).delete(course);
    }
}