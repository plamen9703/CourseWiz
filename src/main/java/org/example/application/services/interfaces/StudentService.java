package org.example.application.services.interfaces;

import org.example.application.api.Student;

import java.util.List;
import java.util.Optional;


public interface StudentService {
    Optional<Student> findById(String pin);

    List<Student> findAll();

    Student create(Student student);

    void update(String studentPin, Student student );

    void delete(String studentPin);

    boolean exists(String studentPin);

}
