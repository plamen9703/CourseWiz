package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Student;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.repository.coursera.StudentRepository;
import org.example.application.services.interfaces.coursera.StudentService;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private static final Function<Student, NotFoundException> STUDENT_NOT_FOUND_EXCEPTION =
            student ->new NotFoundException(
                    String.format(
                            "Student not found with PIN: %s",
                            student.getPin()
                    )
            );

    private static final Function<Student, DuplicateEntityException> STUDENT_DUPLICATE_ENTITY_EXCEPTION =
            student -> new DuplicateEntityException(
                    String.format(
                            "Student with PIN: %s already exists",
                            student.getPin()
                    )
            );
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student findById(Student student) {
        return studentRepository.findById(student)
                .orElseThrow(
                        ()-> STUDENT_NOT_FOUND_EXCEPTION.apply(student)
                );
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student create(Student student) {
        if(studentRepository.existsById(student)){
            throw STUDENT_DUPLICATE_ENTITY_EXCEPTION.apply(student);
        }
       return studentRepository.insert(student);
    }

    @Override
    public void update(Student student) {
        if(!studentRepository.existsById(student))
            throw STUDENT_NOT_FOUND_EXCEPTION.apply(student);
        studentRepository.update(student);
    }

    @Override
    public void delete(Student student) {
        if(!studentRepository.existsById(student))
            throw STUDENT_NOT_FOUND_EXCEPTION.apply(student);
        studentRepository.delete(student);
    }

    @Override
    public boolean existsById(Student student) {
        return studentRepository.existsById(student);
    }
}
