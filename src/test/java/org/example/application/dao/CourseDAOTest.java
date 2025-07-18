package org.example.application.dao;

import org.example.application.api.coursera.Course;
import org.example.application.dao.coursera.CourseDAO;
import org.example.db.JdbcHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CourseDAOTest {


    @Mock
    private JdbcHelper jdbcHelper;

    @InjectMocks
    private org.example.application.dao.coursera.CourseDAO courseDAO;

    @BeforeEach
    void setUp()throws SQLException {
        System.out.println("set up dao: "+jdbcHelper);
        courseDAO = new CourseDAO(jdbcHelper);
    }



    @Test
    void findAll() {
    }

    @Test
    void findById() {
        Course courseExpected = new Course();
        courseExpected.setId(1);
        courseExpected.setName("Java 101");
        System.out.println(jdbcHelper);
        when(jdbcHelper.querySingle(anyString(), any(), any())).thenReturn(Optional.of(courseExpected));
        Course course = new Course();
        course.setId(1);
        Optional<Course> result = courseDAO.findById(course);
        assertTrue(result.isPresent());
        verify(jdbcHelper).querySingle(anyString(), any(), any());
    }

    @Test
    void insert() {
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