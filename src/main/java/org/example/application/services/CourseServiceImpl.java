package org.example.application.services;

import org.example.application.api.Course;
import org.example.application.repository.CourseRepository;
import org.example.application.repository.InstructorRepository;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.services.interfaces.CourseService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    public CourseServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository=instructorRepository;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(int courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Course create(Course course) {
        int id = course.getId();
        Integer courseInstructorId = course.getInstructorId();
        if(courseRepository.existsById(id)){
            throw new DuplicateEntityException(String.format("Course with Id: %d exists.",id));
        }
        if(!instructorRepository.existsById(courseInstructorId)){
            throw new NotFoundException(String.format("Instructor for course does NOT exist. Instructor Id: %d, Course Id:%d",
                    courseInstructorId,id));
        }
        return courseRepository.create(course);
    }

    @Override
    public void update(int courseId, Course course) {
        Course existing = findById(courseId).orElseThrow(()->new NotFoundException(String.format("Course with Id: %d does NOT found.", courseId)));
        Integer courseInstructorId = course.getInstructorId();

        if(!instructorRepository.existsById(courseInstructorId)){
            throw new NotFoundException(String.format("Instructor for course does NOT exist. Instructor Id: %d, Course Id:%d",
                    courseInstructorId, courseId));
        }

        existing.setCredit(course.getCredit());
        existing.setName(course.getName());
        existing.setInstructorId(course.getInstructorId());
        existing.setTotalTime(course.getTotalTime());
    }

    @Override
    public void delete(int courseId) {
        Course course = findById(courseId).orElseThrow(()->new NotFoundException(String.format("Course with Id: %d does NOT found.", courseId)));
        courseRepository.delete(courseId);
    }
}
