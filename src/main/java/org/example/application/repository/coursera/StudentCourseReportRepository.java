package org.example.application.repository.coursera;

import org.example.application.api.coursera.StudentCourseReport;

import java.util.List;

public interface StudentCourseReportRepository {

     List<StudentCourseReport> getStudentCourseReport(List<String> studentPins, Integer minimalCredit, String startDate, String endDate);
}
