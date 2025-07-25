package org.example.application.repository.coursera;

import org.example.application.api.coursera.StudentCourse;
import org.example.application.api.coursera.StudentCourseStatus;
import org.example.application.repository.JdbcRepository;

import java.util.List;

public interface StudentCourseRepository extends JdbcRepository<StudentCourse> {
    List<StudentCourseStatus> getEnrolledStudents(Integer courseId);
}
