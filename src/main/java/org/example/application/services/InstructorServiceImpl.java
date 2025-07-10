package org.example.application.services;

import org.example.application.api.Instructor;
import org.example.application.repository.InstructorRepository;
import org.example.application.services.interfaces.InstructorService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    @Override
    public Optional<Instructor> findById(int instructorId) {
        return instructorRepository.findById(instructorId);
    }

    @Override
    public Instructor create(Instructor instructor) {
        return instructorRepository.create(instructor);
    }

    @Override
    public void update(int instructorId, Instructor instructor) {
        if(!exists(instructorId)){
            throw new NotFoundException("Instructor with ID: %d does NOT exist.".formatted(instructorId));
        }

        int updated = instructorRepository.update(instructorId, instructor);

    }

    @Override
    public void delete(int instructorId) {
        if(!exists(instructorId)){
            throw new NotFoundException(String.format("Instructor with ID: %d does NOT exist.", instructorId));
        }
        instructorRepository.delete(instructorId);
    }

    @Override
    public boolean exists(int instructorId) {
        return instructorRepository.existsById(instructorId);
    }
}
