package org.example.application.services.interfaces.coursera;

import org.example.application.api.coursera.StudentCourseReport;

import java.util.List;

public interface StudentCourseReportService {

    List<StudentCourseReport> getStudentCourseReports(List<String> studentPins, Integer minimalCredit, String startDate, String endDate);

    boolean studentExists(String pin);
}
