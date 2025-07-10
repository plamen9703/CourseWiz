package org.example.application.services.interfaces;

import org.example.application.api.StudentCourseReport;

import java.util.List;

public interface StudentCourseReportService {

    List<StudentCourseReport> getStudentCourseReports(List<String> studentPins, Integer minimalCredit, String startDate, String endDate);
}
