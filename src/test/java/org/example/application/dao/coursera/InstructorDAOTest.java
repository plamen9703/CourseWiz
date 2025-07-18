package org.example.application.dao.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstructorDAOTest {

    static JdbcHelper jdbcHelper=mock(JdbcHelper.class);
    static InstructorDAO instructorDAO;
    @BeforeAll
    static void setUp()  {
        instructorDAO=new InstructorDAO(jdbcHelper);
        when(jdbcHelper.query(any(String.class),any(ResultSetMapper.class))).thenReturn(List.of(
                new Instructor(1,"Plamen", "Hristov", Timestamp.from(Instant.now())),
                new Instructor(),
                new Instructor()
        ));
        when(jdbcHelper.query(anyString(), any(ResultSetMapper.class), eq(1))).thenReturn(List.of(new Instructor(1,"Plamen", "Hristov", Timestamp.from(Instant.now()))));
    }

    @AfterAll
    static void tearDown()  {
    }

    @Test
    void findAll() {
        List<Instructor> instructors=instructorDAO.findAll();
        assertEquals(3, instructors.size());
        assertEquals("Plamen",instructors.get(0).getFirstName());
    }

    @Test
    void findById() {
        Instructor instructorId=new Instructor();
        instructorId.setId(1);
        Optional<Instructor> instructor=instructorDAO.findById(instructorId);

        assertTrue(instructor.isPresent());
        Instructor instructor1=instructor.get();
        assertEquals(1, instructor1.getId());
        assertEquals("Plamen", instructor1.getFirstName());
        assertEquals("Hristov", instructor1.getLastName());
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