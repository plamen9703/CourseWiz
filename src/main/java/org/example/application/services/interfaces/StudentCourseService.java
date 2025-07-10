package org.example.application.services.interfaces;

import org.example.application.api.StudentCourse;

import java.util.List;
import java.util.Optional;

public interface StudentCourseService {
    List<StudentCourse> findAll();

    Optional<StudentCourse> findById(String studentPin, Integer courseId);

    StudentCourse create(StudentCourse studentCourse);

    void update(String studentPin, Integer courseId, StudentCourse studentCourse);

    void delete(String studentPin, Integer courseId);

    boolean exists(String studentPin, Integer courseId);
}
