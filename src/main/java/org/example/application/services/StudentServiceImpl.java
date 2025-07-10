package org.example.application.services;

import org.example.application.api.Student;
import org.example.application.repository.StudentRepository;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.services.interfaces.StudentService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> findById(String pin) {
        return studentRepository.findById(pin);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student create(
            Student student) {
        if(studentRepository.existsById(student.getPin())){
            throw new DuplicateEntityException(String.format("Student with PIN: %s already exists", student.getPin()));
        }
        return studentRepository.create(student);
    }

    @Override
    public void update(String studentPin, Student student) {
        Student existing= findById(studentPin).orElseThrow(()->new NotFoundException(String.format("Student not found with PIN: %s", studentPin) ));
        existing.setFirstName(student.getFirstName());
        existing.setLastName(student.getLastName());
        studentRepository.update(studentPin,existing);
    }

    @Override
    public void delete(String studentPin) {
        if(!exists(studentPin))
            throw new NotFoundException("Student with PIN: %s doesn't exists.".formatted(studentPin));
        studentRepository.delete(studentPin);
    }

    @Override
    public boolean exists(String studentPin) {
        return studentRepository.existsById(studentPin);
    }
}
