package org.example.application.services;

import org.example.application.api.StudentCourse;
import org.example.application.repository.StudentCourseRepository;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.services.interfaces.StudentCourseService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;

    public StudentCourseServiceImpl( StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }

    @Override
    public List<StudentCourse> findAll() {
        return studentCourseRepository.findAll();
    }

    @Override
    public Optional<StudentCourse> findById(String studentPin, Integer courseId) {
        return studentCourseRepository.findById(studentPin,courseId);
    }

    @Override
    public StudentCourse create(StudentCourse studentCourse) {
        if(studentCourseRepository.existById(studentCourse.getStudentPin(), studentCourse.getCourseId())){
            throw new DuplicateEntityException("Student already enrolled in course! Student: %s, Course: %d"
                    .formatted(studentCourse.getStudentPin(),studentCourse.getCourseId()));
        }
        return studentCourseRepository.create(studentCourse);
    }

    @Override
    public void update(String studentPin, Integer courseId, StudentCourse studentCourse) {
        StudentCourse existing = findById(studentPin,courseId).orElseThrow(()->new NotFoundException(String.format(
                "Missing relationship for Student: %s Course: %d", studentPin, courseId)));
        existing.setCompletionDate(studentCourse.getCompletionDate());
        studentCourseRepository.update(studentPin, courseId,studentCourse);
    }

    @Override
    public void delete(String studentPin, Integer courseId) {
        StudentCourse studentCourse = findById(studentPin,courseId).orElseThrow(()->new NotFoundException(String.format(
                "Missing relationship for Student: %s Course: %d", studentPin, courseId)));
        studentCourseRepository.delete(studentPin, courseId);
    }

    @Override
    public boolean exists(String studentPin, Integer courseId) {
        return false;
    }

}
