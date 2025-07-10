package org.example.application.services.interfaces;

import org.example.application.api.Instructor;

import java.util.List;
import java.util.Optional;

public interface InstructorService {

    List<Instructor> findAll();

    Optional<Instructor> findById(int instructorId);

    Instructor create(Instructor instructor);

    void update(int instructorId, Instructor instructor);

    void delete(int instructorId);

    boolean exists(int instructorId);
}
