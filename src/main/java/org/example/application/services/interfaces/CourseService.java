package org.example.application.services.interfaces;

import org.example.application.api.Course;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();

    Optional<Course> findById(int courseId);

    Course create(Course course);

    void update(int courseId, Course course);

    void delete(int courseId);
}
