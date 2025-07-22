package org.example.application.services.interfaces.coursera;

import org.example.application.api.coursera.StudentCourseReport;
import org.example.application.exceptions.StudentCourseReportException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface StudentCourseReportService {

    List<StudentCourseReport> getStudentCourseReports(List<String> studentPins, Integer minimalCredit, String startDate, String endDate) throws StudentCourseReportException;

    boolean studentExists(String pin);

    ByteArrayOutputStream generateExcelReport(List<StudentCourseReport> reports) throws IOException;
}
