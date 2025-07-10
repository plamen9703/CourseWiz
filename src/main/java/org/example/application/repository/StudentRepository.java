package org.example.application.repository;

import org.example.application.api.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    List<Student> findAll();

    Optional<Student> findById(String studentPin);

    Student create(Student student);

    void update(String studentPin, Student student);

    void delete(String studentPin);

    boolean existsById(String studentPin);
}
