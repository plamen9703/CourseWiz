package org.example.application.repository;

import org.example.application.api.Instructor;
import java.util.*;

public interface InstructorRepository {

    List<Instructor> findAll();

    Optional<Instructor> findById(int instructorId);

    Instructor create(Instructor instructor);

    int update(Integer instructorId, Instructor instructor);

    int delete(Integer instructorId);

    boolean existsById(int instructorId);
}
