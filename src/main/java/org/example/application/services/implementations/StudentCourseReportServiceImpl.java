package org.example.application.services.implementations;

import org.example.application.api.StudentCourseReport;
import org.example.application.repository.StudentCourseReportRepository;
import org.example.application.services.interfaces.StudentCourseReportService;

import java.util.List;

public class StudentCourseReportServiceImpl implements StudentCourseReportService {
    private final StudentCourseReportRepository studentCourseReportRepository;

    public StudentCourseReportServiceImpl(StudentCourseReportRepository studentCourseReportRepository) {
        this.studentCourseReportRepository = studentCourseReportRepository;
    }

    @Override
    public List<StudentCourseReport> getStudentCourseReports(List<String> studentPins, Integer minimalCredit, String startDate, String endDate) {
        return studentCourseReportRepository.getStudentCourseReport(studentPins, minimalCredit, startDate, endDate);
    }
}
