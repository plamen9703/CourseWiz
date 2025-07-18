package org.example.application.services.implementations.coursera;

import org.example.application.api.coursera.Student;
import org.example.application.api.coursera.StudentCourseReport;
import org.example.application.repository.coursera.StudentCourseReportRepository;
import org.example.application.repository.coursera.StudentRepository;
import org.example.application.services.interfaces.coursera.StudentCourseReportService;

import java.util.List;

public class StudentCourseReportServiceImpl implements StudentCourseReportService {
    private final StudentCourseReportRepository studentCourseReportRepository;
    private final StudentRepository studentRepository;
    public StudentCourseReportServiceImpl(StudentCourseReportRepository studentCourseReportRepository, StudentRepository studentRepository) {
        this.studentCourseReportRepository = studentCourseReportRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentCourseReport> getStudentCourseReports(List<String> studentPins, Integer minimalCredit, String startDate, String endDate) {
        return studentCourseReportRepository.getStudentCourseReport(studentPins, minimalCredit, startDate, endDate);
    }

    @Override
    public boolean studentExists(String pin) {
        Student student=new Student();
        student.setPin(pin);
        return studentRepository.existsById(student);
    }
}
