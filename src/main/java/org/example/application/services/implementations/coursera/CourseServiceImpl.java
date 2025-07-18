package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Course;
import org.example.application.api.coursera.Instructor;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.repository.coursera.CourseRepository;
import org.example.application.repository.coursera.InstructorRepository;
import org.example.application.services.interfaces.coursera.CourseService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    private static final Function<Course, NotFoundException> COURSE_NOT_FOUND_EXEPTION =
            course -> new NotFoundException(
                    String.format(
                            "Course with ID: %d not found.",
                            course.getId()
                    )
            );

    private static final Function<Course, DuplicateEntityException> COURSE_DUPLICATE_ENTITY_EXCEPTION=
            course -> new DuplicateEntityException(
                    String.format(
                            "Course with ID: %d already exists",
                            course.getId()
                    )
            );
    private static final Function<Instructor, NotFoundException> INSTRUCTOR_NOT_FOUND_EXCEPTION=
            instructor -> new NotFoundException(
                    String.format(
                            "Instructor with ID: %d not found.",
                            instructor.getId()
                    )
            );

    public CourseServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository=instructorRepository;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(Course course) {
        return courseRepository
                .findById(course)
                .orElseThrow(
                        () ->COURSE_NOT_FOUND_EXEPTION.apply(course)
                );
    }

    @Override
    public Course create(Course course) {
        if (courseRepository.existsById(course)) {
            throw COURSE_DUPLICATE_ENTITY_EXCEPTION.apply(course);
        }
        Instructor instructor = new Instructor(course.getInstructorId(), null, null, null);
        if (!instructorRepository.existsById(instructor)) {
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        }
        return courseRepository.insert(course);
    }

    @Override
    public void update(Course course) {
        if (!existsById(course)) {
            throw COURSE_NOT_FOUND_EXEPTION.apply(course);
        }
        if (course.getInstructorId() == null) {
            course = findById(course);
            Instructor instructor = new Instructor(
                    course.getInstructorId(),
                    null,
                    null,
                    null
            );
            if (!instructorRepository.existsById(instructor)) {
                throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
            }
        }
        course.setCredit(course.getCredit());
        course.setName(course.getName());
        course.setInstructorId(course.getInstructorId());
        course.setTotalTime(course.getTotalTime());
    }

    @Override
    public void delete(Course course) {
        if (!existsById(course))
            throw COURSE_NOT_FOUND_EXEPTION.apply(course);
        courseRepository.delete(course);
    }

    @Override
    public boolean existsById(Course course) {
        return courseRepository.existsById(course);
    }
}
