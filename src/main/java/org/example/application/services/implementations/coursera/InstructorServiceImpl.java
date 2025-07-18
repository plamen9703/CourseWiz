package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.repository.coursera.InstructorRepository;
import org.example.application.services.interfaces.coursera.InstructorService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    private static final Function<Instructor, DuplicateEntityException> INSTRUCTOR_DUPLICATE_ENTITY_EXCEPTION =
            instructor -> new DuplicateEntityException(
                    String.format(
                            "Instructor with ID: %d already exists",
                            instructor.getId()
                    )
            );

    private static final Function<Instructor, NotFoundException> INSTRUCTOR_NOT_FOUND_EXCEPTION =
            instructor -> new NotFoundException(
                    String.format(
                            "Instructor with ID: %d does NOT exist.",
                            instructor.getId()
                    )
            );


    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor findById(Instructor instructor) {
        return instructorRepository.findById(instructor)
                .orElseThrow(
                        () -> INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor)
                );
    }

    @Override
    public Instructor create(Instructor instructor) {
        if (instructorRepository.existsById(instructor))
            throw INSTRUCTOR_DUPLICATE_ENTITY_EXCEPTION.apply(instructor);
        return instructorRepository.insert(instructor);
    }

    @Override
    public void update(Instructor instructor) {
        if (!instructorRepository.existsById(instructor)) {
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        }

        instructorRepository.update(instructor);

    }

    @Override
    public void delete(Instructor instructor) {
        if (!instructorRepository.existsById(instructor)) {
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        }
        instructorRepository.delete(instructor);
    }

    @Override
    public boolean existsById(Instructor instructor) {
        return instructorRepository.existsById(instructor);
    }
}
