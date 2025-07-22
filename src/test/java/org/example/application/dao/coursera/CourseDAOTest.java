package org.example.application.dao.coursera;

import org.example.application.api.coursera.Course;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CourseDAOTest {

    private JdbcHelper jdbcHelper = mock(JdbcHelper.class);

    private CourseDAO courseDAO;

    @BeforeEach
    void setUp(){
        courseDAO= new CourseDAO(jdbcHelper);
    }

    @AfterEach
    void tearDown() {
        reset(jdbcHelper);
    }

    @Test
    void findAll() {
        String sql = "SELECT id, name, instructor_id, total_time, credit, time_created FROM coursera.courses;";
        when(jdbcHelper.query(anyString(),any(ResultSetMapper.class))).thenReturn(List.of());
        courseDAO.findAll();
        verify(jdbcHelper)
                .query(
                    eq(sql),
                    eq(CourseDAO.COURSE_RESULT_SET_MAPPER)
                );
    }

    @Test
     void findById() {
        String sql = "SELECT id, name, instructor_id, total_time, credit, time_created FROM coursera.courses WHERE id=?;";
        when(jdbcHelper.querySingle(anyString(), any(ResultSetMapper.class),anyInt())).thenReturn(Optional.ofNullable(null));
        Optional<Course> optionalInstructor = courseDAO.findById(new Course(1, null, null, null, null, null));

        assertTrue(optionalInstructor.isEmpty());
        verify(jdbcHelper).querySingle(eq(sql),eq(CourseDAO.COURSE_RESULT_SET_MAPPER), eq(1));
    }

    @Test
     void insert() {
        String sql = "INSERT INTO coursera.courses(name, instructor_id, total_time, credit) VALUES (?, ?, ?, ?);";
        when(jdbcHelper.insert(anyString(), any(ResultSetMapper.class), anyString(),anyInt(), anyInt(), anyInt())).thenReturn(1);
        courseDAO.insert(new Course(null, "Java 101", 2,(short)2,(short)2, Timestamp.from(Instant.now())));
        verify(jdbcHelper)
                .insert(
                        eq(sql),
                        eq(CourseDAO.COURSE_RESULT_SET_MAPPER),
                        eq("Java 101"),
                        eq(2),
                        eq((short) 2),
                        eq((short) 2)
                );
    }

    @Test
     void update() {
        String sql = "UPDATE coursera.courses SET name=?, instructor_id=?, total_time=?, credit=? WHERE id=?;";
        when(jdbcHelper.update(anyString(),anyString(), anyInt(), anyShort(), anyShort())).thenReturn(1);
        courseDAO.update(new Course(1, "Java 101", 1, (short)20, (short)20,null));
        verify(jdbcHelper)
                .update(
                        eq(sql),
                        eq("Java 101"),
                        eq(1),
                        eq((short) 20),
                        eq((short) 20),
                        eq(1)
                );
    }

    @Test
     void delete() {
        String sql = "DELETE FROM coursera.courses WHERE id=?;";
        when(jdbcHelper.update(anyString(), anyInt())).thenReturn(1);
        courseDAO.delete(new Course(1, null, null, null, null,null));
        verify(jdbcHelper)
                .update(
                        eq(sql),
                        eq(1)
                );
    }

    @Test
     void existsById() {
        String sql = "SELECT id, name, instructor_id, total_time, credit, time_created FROM coursera.courses WHERE id=?;";
        when(jdbcHelper.exists(anyString(),anyInt())).thenReturn(true);
        Course course = new Course();
        course.setId(1);
        courseDAO.existsById(course);
        verify(jdbcHelper)
                .exists(
                        eq(sql),
                        eq(1)
                );
    }

    @Test
    void mapResultSetForCourse() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Java 101");
        when(rs.getInt("instructor_id")).thenReturn(1);
        when(rs.getShort("total_time")).thenReturn((short)60);
        when(rs.getShort("credit")).thenReturn((short)5);
        Timestamp timestamp = Timestamp.valueOf("2025-07-21 13:35:45");
        when(rs.getTimestamp("time_created")).thenReturn(timestamp);

        Course course =CourseDAO.COURSE_RESULT_SET_MAPPER.map(rs);
        assertNotNull(course);
        verify(rs).getInt("id");
        verify(rs).getString("name");
        verify(rs).getInt("instructor_id");
        verify(rs).getShort("total_time");
        verify(rs).getShort("credit");
        verify(rs).getTimestamp("time_created");
    }
}