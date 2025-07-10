package org.example.application.repository;

import org.example.application.api.StudentCourseReport;

import java.util.List;

public interface StudentCourseReportRepository {

     List<StudentCourseReport> getStudentCourseReport(List<String> studentPins, Integer minimlaCredit, String startDate, String endDate);
}
