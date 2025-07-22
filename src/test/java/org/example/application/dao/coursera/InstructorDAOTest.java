package org.example.application.dao.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class InstructorDAOTest {

    static JdbcHelper jdbcHelper=mock(JdbcHelper.class);
    static InstructorDAO instructorDAO;
    @BeforeEach
    void setUp()  {
        instructorDAO=new InstructorDAO(jdbcHelper);
    }

    @AfterEach
    void tearDown()  {
        reset(jdbcHelper);
    }

    @Test
    void findAll() {
        String query = "SELECT id, first_name, last_name, time_created FROM coursera.instructors;";
        when(jdbcHelper.query(anyString(), any()))
                .thenReturn(List.of(
                        new Instructor(1,"Plamen", "Hristov", Timestamp.from(Instant.now())),
                        new Instructor(2,"Stefan", "Lazarov", Timestamp.from(Instant.now())),
                        new Instructor(3,"Jhon", "Doe", Timestamp.from(Instant.now()))
                ));
        List<Instructor> instructors=instructorDAO.findAll();
        assertEquals(3, instructors.size());
        assertEquals("Plamen",instructors.get(0).getFirstName());
        assertEquals("Hristov", instructors.get(0).getLastName());
        assertEquals(1, instructors.get(0).getId());
        assertEquals("Jhon", instructors.get(2).getFirstName());
        verify(jdbcHelper).query(
                eq("SELECT id, first_name, last_name, time_created FROM coursera.instructors;"),
                eq(InstructorDAO.INSTRUCTOR_RESULT_SET_MAPPER)
        );

    }

    @Test
    void findById() {
        Instructor instructorId=new Instructor();
        instructorId.setId(1);
        String query = "SELECT id, first_name, last_name, time_created FROM coursera.instructors WHERE id=?;";

        when(jdbcHelper.querySingle(
                anyString(),
                any(ResultSetMapper.class),
                anyInt()))
                .thenReturn(Optional.of(new Instructor(1, "Plamen", "Hristov", Timestamp.from(Instant.now()))));
        Optional<Instructor> instructor=instructorDAO.findById(instructorId);
        assertTrue(instructor.isPresent());
        Instructor instructor1=instructor.get();
        assertEquals(1, instructor1.getId());
        assertEquals("Plamen", instructor1.getFirstName());
        assertEquals("Hristov", instructor1.getLastName());
        verify(jdbcHelper).querySingle(eq(query),eq(InstructorDAO.INSTRUCTOR_RESULT_SET_MAPPER),eq(1));
    }

    @Test
    void insert() {
        Instructor instructor=new Instructor();
        instructor.setFirstName("Stefan");
        instructor.setLastName("Hristov");
        ArgumentCaptor<String> captor=ArgumentCaptor.forClass(String.class);

        when(jdbcHelper.insert(anyString(), any(ResultSetMapper.class), anyString(), anyString()))
                .thenReturn(instructor);

        instructorDAO.insert(instructor);
        String query = "INSERT INTO coursera.instructors(first_name, last_name) VALUES (?, ?);";

        verify(jdbcHelper).insert(
                eq(query),
                eq(InstructorDAO.INSTRUCTOR_RESULT_SET_MAPPER),
                captor.capture(),
                captor.capture()
                );
        List<String> arguments = captor.getAllValues();
        assertEquals(2,arguments.size());
        assertEquals("Stefan",arguments.get(0));
        assertEquals("Hristov", arguments.get(1));
    }

    @Test
    void update() {
        Instructor expexted = new Instructor();
        expexted.setId(1);
        expexted.setFirstName("Iliana");
        expexted.setLastName("Hristova");
        when(jdbcHelper.update(anyString(),anyString(), anyString())).thenReturn(1);
        instructorDAO.update(expexted);
        String sql="UPDATE coursera.instructors SET first_name=?, last_name=? WHERE id=?;";
        verify(jdbcHelper).update(
                eq(sql),
                eq("Iliana"),
                eq("Hristova"),
                eq(1)
        );
    }

    @Test
    void delete() {
        String sql = "DELETE FROM coursera.instructors WHERE id = ?;";
        when(jdbcHelper.update(anyString(), anyInt())).thenReturn(1);
        instructorDAO.delete(new Instructor(1,null,null, null));
        verify(jdbcHelper).update(eq(sql),eq(1));
    }

    @Test
    void existsById() {
        String sql= "SELECT id, first_name, last_name, time_created FROM coursera.instructors WHERE id=?;";
        when(jdbcHelper.exists(any(), any())).thenReturn(true);
        instructorDAO.existsById(new Instructor(1, null,null,null));
        verify(jdbcHelper).exists(eq(sql),eq(1));
    }

    @Test
    void mapsResultSetToInstructor() throws Exception {
        // Arrange: mock ResultSet
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("first_name")).thenReturn("Plamen");
        when(rs.getString("last_name")).thenReturn("Hristov");
        Timestamp timestamp = Timestamp.valueOf("2025-07-21 10:15:30");
        when(rs.getTimestamp("time_created")).thenReturn(timestamp);

        // Act: use the mapper
        Instructor instructor = InstructorDAO.INSTRUCTOR_RESULT_SET_MAPPER.map(rs);

        // Assert
        assertNotNull(instructor);
        assertEquals(1, instructor.getId());
        assertEquals("Plamen", instructor.getFirstName());
        assertEquals("Hristov", instructor.getLastName());
        assertEquals(timestamp, instructor.getTimeCreated());

        // Verify the ResultSet was accessed correctly
        verify(rs).getInt("id");
        verify(rs).getString("first_name");
        verify(rs).getString("last_name");
        verify(rs).getTimestamp("time_created");
    }
}