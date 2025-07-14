package org.example.application.services.implementations;

import org.example.application.api.StudentCourse;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.repository.StudentCourseRepository;
import org.example.application.services.interfaces.StudentCourseService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;

    private static final Function<StudentCourse, NotFoundException> STUDENT_COURSE_NOT_FOUND_EXCEPTION =
            studentCourse -> new NotFoundException(
                    String.format(
                            "Student with PIN: %s is not enrolled in course with ID: %d",
                            studentCourse.getStudentPin(),
                            studentCourse.getCourseId()
                    )
            );

    private static final Function<StudentCourse, DuplicateEntityException> STUDENT_COURSE_DUPLICATE_ENTITY_EXCEPTION =
            studentCourse -> new DuplicateEntityException(
                    String.format(
                            "Student already enrolled in course! Student: %s, Course: %d",
                            studentCourse.getStudentPin(),
                            studentCourse.getCourseId()
                    )
            );

    public StudentCourseServiceImpl( StudentCourseRepository studentCourseRepository) {
        this.studentCourseRepository = studentCourseRepository;
    }

    @Override
    public List<StudentCourse> findAll() {
        return studentCourseRepository.findAll();
    }

    @Override
    public StudentCourse findById(StudentCourse studentCourse) {
        return studentCourseRepository
                .findById(studentCourse)
                .orElseThrow(
                        () -> STUDENT_COURSE_NOT_FOUND_EXCEPTION.apply(studentCourse)
                );
    }

    @Override
    public StudentCourse create(StudentCourse studentCourse) {
        if (studentCourseRepository.existsById(studentCourse)) {
            throw STUDENT_COURSE_DUPLICATE_ENTITY_EXCEPTION.apply(studentCourse);
        }
        return studentCourseRepository.insert(studentCourse);
    }

    @Override
    public void update(StudentCourse studentCourse) {
        if (!studentCourseRepository.existsById(studentCourse)) {
            throw STUDENT_COURSE_NOT_FOUND_EXCEPTION.apply(studentCourse);
        }
        studentCourseRepository.update(studentCourse);
    }

    @Override
    public void delete(StudentCourse studentCourse) {
        if (!studentCourseRepository.existsById(studentCourse))
            throw STUDENT_COURSE_NOT_FOUND_EXCEPTION.apply(studentCourse);
        studentCourseRepository.delete(studentCourse);
    }

    @Override
    public boolean existsById(StudentCourse studentCourse) {
        return studentCourseRepository.existsById(studentCourse);
    }

}
