package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.application.dao.coursera.InstructorDAO;
import org.example.application.services.interfaces.coursera.InstructorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class InstructorServiceImplTest {

    private final InstructorDAO instructorDAO= mock(InstructorDAO.class);

    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        instructorService=new InstructorServiceImpl(instructorDAO);
    }

    @AfterEach
    void tearDown() {
        reset(instructorDAO);
    }

    @Test
    void findAll() {
        when(instructorDAO.findAll()).thenReturn(List.of(new Instructor(null,"Plamen", null, null)));
        List<Instructor> all = instructorService.findAll();
        assertEquals(1,all.size());
        assertEquals("Plamen", all.get(0).getFirstName());
    }

    @Test
    void findById() {
        when(instructorDAO.findById(any(Instructor.class))).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> instructorService.findById(new Instructor()));
        assertEquals("Instructor with ID: null does NOT exist.",exception.getMessage());
    }

    @Test
    void create() {
        Instructor instructorTest = new Instructor();
        instructorTest.setFirstName("Plamen");
        instructorTest.setLastName("Hristov");
        Instructor instructorReturned=new Instructor();
        instructorReturned.setId(1);
        instructorReturned.setFirstName("Plamen");
        instructorReturned.setLastName("Hristov");
        when(instructorDAO.insert(instructorTest)).thenReturn(instructorReturned);
        Instructor instructorCreated = instructorService.create(instructorTest);
        assertEquals(instructorReturned, instructorCreated);
    }

    @Test
    void updateWhenInstructorIdIsNotValid() {
        Instructor instructorId=new Instructor();
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> instructorService.update(instructorId));
        instructorId.setId(0);
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> instructorService.update(instructorId));
        instructorId.setId(-1);
        RuntimeException exception3 = assertThrows(RuntimeException.class, () -> instructorService.update(instructorId));
        assertEquals("Instructor with ID: null does NOT exist.", exception1.getMessage());
        assertEquals("Instructor with ID: 0 does NOT exist.", exception2.getMessage());
        assertEquals("Instructor with ID: -1 does NOT exist.", exception3.getMessage());
        verify(instructorDAO, never()).update(any(Instructor.class));
    }

    @Test
    void updateWhenInstructorIdIsValid() {
        Instructor instructorId=new Instructor();
        instructorId.setId(1);
        when(instructorDAO.existsById(instructorId)).thenReturn(true);
        instructorService.update(instructorId);
        verify(instructorDAO,times(1)).update(instructorId);
    }

    @Test
    void deleteWhenIdIsNotValid() {
        Instructor instructorId=new Instructor();
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> instructorService.delete(instructorId));
        instructorId.setId(0);
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> instructorService.delete(instructorId));
        instructorId.setId(-1);
        RuntimeException exception3 = assertThrows(RuntimeException.class, () -> instructorService.delete(instructorId));
        when(instructorDAO.existsById(instructorId)).thenReturn(false);
        instructorId.setId(1);
        RuntimeException exception4 = assertThrows(RuntimeException.class, () -> instructorService.delete(instructorId));
        assertEquals("Instructor with ID: null does NOT exist.", exception1.getMessage());
        assertEquals("Instructor with ID: 0 does NOT exist.", exception2.getMessage());
        assertEquals("Instructor with ID: -1 does NOT exist.", exception3.getMessage());
        assertEquals("Instructor with ID: 1 does NOT exist.", exception4.getMessage());
        verify(instructorDAO, never()).delete(any(Instructor.class));

    }
    @Test
    void deleteWhenIdIsValid() {
        Instructor instructorId=new Instructor();
        instructorId.setId(1);
        when(instructorDAO.existsById(instructorId)).thenReturn(true);
        instructorService.delete(instructorId);
        verify(instructorDAO, times(1)).delete(instructorId);
    }

    @Test
    void existsByIdWhenIdIsNotValid() {
        Instructor instructorId=new Instructor();
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> instructorService.existsById(instructorId));
        instructorId.setId(0);
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> instructorService.existsById(instructorId));
        instructorId.setId(-1);
        RuntimeException exception3 = assertThrows(RuntimeException.class, () -> instructorService.existsById(instructorId));
        assertEquals("Instructor with ID: null does NOT exist.", exception1.getMessage());
        assertEquals("Instructor with ID: 0 does NOT exist.", exception2.getMessage());
        assertEquals("Instructor with ID: -1 does NOT exist.", exception3.getMessage());
        verify(instructorDAO,never()).existsById(any(Instructor.class));
    }
    @Test
    void existsByIdWhenIdIsValid() {
        Instructor instructorId=new Instructor();
        instructorId.setId(1);
        when(instructorDAO.existsById(instructorId)).thenReturn(true);
        assertTrue(instructorService.existsById(instructorId));
        verify(instructorDAO,times(1)).existsById(instructorId);
    }
}