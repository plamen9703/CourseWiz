package org.example.application.repository;

import org.example.application.api.Course;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    List<Course> findAll();

    Optional<Course> findById(int courseId);

    Course create(Course course);

    int update(Integer courseId, Course course);

    int delete(Integer courseId);

    boolean existsById(int courseId);
}
