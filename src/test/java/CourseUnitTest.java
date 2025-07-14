import org.example.application.api.Course;
import org.example.application.api.Instructor;
import org.example.application.dao.CourseDAO;
import org.example.application.dao.InstructorDAO;
import org.example.application.repository.CourseRepository;
import org.example.application.repository.InstructorRepository;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.services.implementations.CourseServiceImpl;
import org.example.application.services.interfaces.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CourseUnitTest {


    private CourseRepository courseRepository;
    private CourseService courseService;
    private InstructorRepository instructorRepository;

    @BeforeEach
    public void setup(){
        courseRepository=mock(CourseDAO.class);
        instructorRepository=mock(InstructorDAO.class);
        courseService = new CourseServiceImpl(courseRepository, instructorRepository);
    }

    @Test
    void create_whenCourseAlreadyExists_shouldThrowDuplicateEntityException() {
        Course course = new Course();
        course.setId(1);
        Instructor instructor = new Instructor();
        instructor.setId(100);
        course.setInstructorId(100);

        when(courseRepository.existsById(1)).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> courseService.create(course));

        verify(courseRepository).existsById(1);
        verify(instructorRepository, never()).existsById(anyInt());
        verify(courseRepository, never()).create(any());
    }

    @Test
    void create_whenInstructorDoesNotExist_shouldThrowNotFoundException() {
        Course course = new Course();
        course.setId(2);
        Instructor instructor = new Instructor();
        instructor.setId(200);
        course.setInstructorId(200);

        when(courseRepository.existsById(2)).thenReturn(false);
        when(instructorRepository.existsById(200)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> courseService.create(course));

        verify(courseRepository).existsById(2);
        verify(instructorRepository).existsById(200);
        verify(courseRepository, never()).create(any());
    }

    @Test
    void create_whenValid_shouldCreateCourse() {
        Course course = new Course();
        course.setId(3);
        Instructor instructor = new Instructor();
        instructor.setId(300);
        course.setInstructorId(300);

        when(courseRepository.existsById(3)).thenReturn(false);
        when(instructorRepository.existsById(300)).thenReturn(true);
        when(courseRepository.create(course)).thenReturn(course);

        Course result = courseService.create(course);

        assertEquals(course, result);

        verify(courseRepository).existsById(3);
        verify(instructorRepository).existsById(300);
        verify(courseRepository).create(course);
    }
}
