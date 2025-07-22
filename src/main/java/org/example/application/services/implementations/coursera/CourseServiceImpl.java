package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Course;
import org.example.application.api.coursera.Instructor;
import org.example.application.repository.coursera.CourseRepository;
import org.example.application.repository.coursera.InstructorRepository;
import org.example.application.services.interfaces.coursera.CourseService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    private static final Function<Course, NotFoundException> COURSE_NOT_FOUND_EXCEPTION =
            course -> new NotFoundException(
                    String.format(
                            "Course with ID: %d not found.",
                            course.getId()
                    )
            );

//    private static final Function<Course, DuplicateEntityException> COURSE_DUPLICATE_ENTITY_EXCEPTION=
//            course -> new DuplicateEntityException(
//                    String.format(
//                            "Course with ID: %d already exists",
//                            course.getId()
//                    )
//            );
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
        if(course.getId()==null || course.getId()<=0)
            throw COURSE_NOT_FOUND_EXCEPTION.apply(course);
        return courseRepository
                .findById(course)
                .orElseThrow(
                        () -> COURSE_NOT_FOUND_EXCEPTION.apply(course)
                );
    }

    @Override
    public Course create(Course course) {
        Instructor instructor = new Instructor();
        instructor.setId(course.getInstructorId());
        if (!instructorRepository.existsById(instructor)) {
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        }
        return courseRepository.insert(course);
    }

    @Override
    public void update(Course course) {
        //get the existing course and throw an exception if it doesn't exists
        Course existingCourse = findById(course);
        //if the instructor id is null then it is not changed
        if (course.getInstructorId() == null) {
            //since the id is not changing
            //take the already exiting id from the database and put it on the course
            course.setInstructorId(existingCourse.getInstructorId());
        }else{
            //if the instructor id is being changed check if the new id exists in the database
            Instructor instructor = new Instructor();
            instructor.setId(course.getInstructorId());
            if (!instructorRepository.existsById(instructor)) {
                throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
            }
        }
        courseRepository.update(course);
    }

    @Override
    public void delete(Course course) {
        if (!existsById(course))
            throw COURSE_NOT_FOUND_EXCEPTION.apply(course);
        courseRepository.delete(course);
    }

    @Override
    public boolean existsById(Course course) {
        if(course.getId()==null || course.getId()<=0)
            throw COURSE_NOT_FOUND_EXCEPTION.apply(course);
        return courseRepository.existsById(course);
    }
}
