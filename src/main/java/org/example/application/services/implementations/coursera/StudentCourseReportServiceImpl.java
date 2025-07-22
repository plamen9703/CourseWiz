package org.example.application.services.implementations.coursera;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.application.api.coursera.Student;
import org.example.application.api.coursera.StudentCourseReport;
import org.example.application.exceptions.StudentCourseReportException;
import org.example.application.repository.coursera.StudentCourseReportRepository;
import org.example.application.repository.coursera.StudentRepository;
import org.example.application.services.interfaces.coursera.StudentCourseReportService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseReportServiceImpl implements StudentCourseReportService {

    private final StudentCourseReportRepository studentCourseReportRepository;

    private final StudentRepository studentRepository;
    public StudentCourseReportServiceImpl(StudentCourseReportRepository studentCourseReportRepository, StudentRepository studentRepository) {
        this.studentCourseReportRepository = studentCourseReportRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentCourseReport> getStudentCourseReports(
            List<String> studentPins,
            Integer minimalCredit,
            String startDate,
            String endDate) throws StudentCourseReportException {

        validateStudentPins(studentPins);
        if (minimalCredit != null && minimalCredit < 0)
            throw new StudentCourseReportException("Minimal required credit can NOT be less than 0.");
        validateDates(startDate, endDate);
        return studentCourseReportRepository
                .getStudentCourseReport(studentPins, minimalCredit, startDate, endDate);
    }

    private void validateStudentPins(List<String> studentPins) {
        if (studentPins == null)
            return;
        List<String> invalid = getInvalidStudentPins(studentPins);
        if (!invalid.isEmpty()) {
            throw new StudentCourseReportException(String.format("Invalid student pins: %s", invalid));
        }

    }

    private static void validateDates(String startDate, String endDate) throws StudentCourseReportException{
        if (startDate == null || endDate == null)
            return;
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);
        if (start.after(end)) {
            throw new StudentCourseReportException("End date must be after start date.");
        }
    }

    private List<String> getInvalidStudentPins(List<String> studentPins) {
        List<String> invalid = new ArrayList<>();
        for (String pin : studentPins) {
            if (!studentExists(pin)) {
                invalid.add(pin);
            }
        }
        return invalid;
    }

    @Override
    public ByteArrayOutputStream generateExcelReport(List<StudentCourseReport> reportData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Report");

        int rowNum = 1;
        String prevStudentPin = null;

        final String[] STUDENT_HEADERS = {
                "Student Name", "Total Credit"
        };

        final String[] COURSE_HEADERS = {
                "Completed",
                "Course Name", "Total Time",
                "Credit", "Instructor Name"
        };

        for (StudentCourseReport record : reportData) {
            if (prevStudentPin == null || !prevStudentPin.equals(record.getStudentPin())) {
                Row studentHeaderRow = sheet.createRow(rowNum++);
                createHeaders(studentHeaderRow, STUDENT_HEADERS);

                Row studentRow = sheet.createRow(rowNum++);
                studentRow.createCell(0).setCellValue(record.getStudentName());
                studentRow.createCell(1).setCellValue(record.getTotalCredit());

                rowNum = createHeaders(sheet.createRow(rowNum++), COURSE_HEADERS, rowNum);
                prevStudentPin = record.getStudentPin();
            }

            Row courseRow = sheet.createRow(rowNum++);
            courseRow.createCell(0).setCellValue("-->");
            courseRow.createCell(1).setCellValue(record.getCourseName());
            courseRow.createCell(2).setCellValue(record.getTotalTime());
            courseRow.createCell(3).setCellValue(record.getCredit());
            courseRow.createCell(4).setCellValue(record.getInstructorName());
        }

        for (int i = 0; i < STUDENT_HEADERS.length + COURSE_HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
    }

    private static void createHeaders(Row row, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
    }

    private static int createHeaders(Row row, String[] headers, int rowNum) {
        createHeaders(row, headers);
        return rowNum;
    }

    @Override
    public boolean studentExists(String pin) {
        Student student=new Student();
        student.setPin(pin);
        return studentRepository.existsById(student);
    }
}
