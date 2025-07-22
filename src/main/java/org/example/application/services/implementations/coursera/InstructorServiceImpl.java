package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.application.repository.coursera.InstructorRepository;
import org.example.application.services.interfaces.coursera.InstructorService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

//    private static final Function<Instructor, DuplicateEntityException> INSTRUCTOR_DUPLICATE_ENTITY_EXCEPTION =
//            instructor -> new DuplicateEntityException(
//                    String.format(
//                            "Instructor with ID: %d already exists",
//                            instructor.getId()
//                    )
//            );

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
        return instructorRepository.insert(instructor);
    }

    @Override
    public void update(Instructor instructor) {
        if (verifyId(instructor)) {
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        }
        instructorRepository.update(instructor);

    }

    @Override
    public void delete(Instructor instructor) {
        boolean isValid = verifyId(instructor);
        if (isValid) {
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        }
        instructorRepository.delete(instructor);
    }

    private boolean verifyId(Instructor instructor) {
        return instructor.getId() == null || instructor.getId() <= 0 || !instructorRepository.existsById(instructor);
    }

    @Override
    public boolean existsById(Instructor instructor) {
        Integer id = instructor.getId();
        if(id==null || id<=0)
            throw INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(instructor);
        return instructorRepository.existsById(instructor);
    }
}
