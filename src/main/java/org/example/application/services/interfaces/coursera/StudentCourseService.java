package org.example.application.services.interfaces.coursera;

import org.example.application.api.coursera.StudentCourse;
import org.example.application.api.coursera.StudentCourseStatus;
import org.example.application.services.interfaces.JdbcService;

import java.util.List;

public interface StudentCourseService extends JdbcService<StudentCourse> {

    List<StudentCourseStatus> getEnrolledStudents(Integer courseId);
}
