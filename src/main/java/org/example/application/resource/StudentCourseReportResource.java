package org.example.application.resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.application.api.StudentCourseReport;
import org.example.application.services.interfaces.StudentCourseReportService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Path("/reports")
@Produces("text/csv")
@RolesAllowed({"course-admin", "instructor-admin","student-admin"})
public class StudentCourseReportResource {
    private final StudentCourseReportService studentCourseReportService;
    private static final String[] STUDENT_HEADERS = {
            "Student Name", "Total Credit"
    };

    private static final String[] COURSE_HEADERS = {
            "Completed",
            "Course Name", "Total Time",
            "Credit", "Instructor Name"
    };

    public StudentCourseReportResource(StudentCourseReportService studentCourseReportService) {
        this.studentCourseReportService = studentCourseReportService;
    }

    @GET
    @Path("/student-course")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getStudentCourseReport(
            @QueryParam("students") List<String> studentPins,
            @QueryParam("minCredit") Integer minCredit,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
    ) {

        // -- CSV Export logic (optional/legacy) --
    /*
    StreamingOutput stream = output -> {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM get_student_course_report(?, ?, ?, ?)"
             )) {

            Array pinsArray = studentPins.isEmpty() ? null :
                    conn.createArrayOf("varchar", studentPins.toArray(new String[0]));
            stmt.setArray(1, pinsArray);
            stmt.setObject(2, minCredit, Types.INTEGER);
            stmt.setObject(3, startDate != null ? Date.valueOf(startDate) : null, Types.DATE);
            stmt.setObject(4, endDate != null ? Date.valueOf(endDate) : null, Types.DATE);

            try (ResultSet rs = stmt.executeQuery();
                 CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), CSVFormat.DEFAULT)) {

                String prevStudentPin = null;
                while (rs.next()) {
                    String studentPin = rs.getString("Student PIN");
                    String studentName = rs.getString("Student Name");
                    int totalCredit = rs.getInt("Total Credit");
                    String courseName = rs.getString("Course Name");
                    int totalTime = rs.getInt("Total Time");
                    int credit = rs.getInt("Credit");
                    String instructorName = rs.getString("Instructor Name");

                    if (prevStudentPin == null || !prevStudentPin.equals(studentPin)) {
                        prevStudentPin = studentPin;
                        printer.printRecord("Student Name", "Total Credit");
                        printer.printRecord(studentName, totalCredit);
                        printer.printRecord("Completed:", "Course Name:", "Course Credit:", "Total Time:", "Instructor:");
                    }

                    printer.printRecord("-->", courseName, totalTime, credit, instructorName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    return Response.ok(stream)
            .header("Content-Disposition", "attachment; filename=\"student_course_report.csv\"")
            .build();
    */

        // -- Excel Export Logic --
        List<StudentCourseReport> reports = studentCourseReportService.getStudentCourseReports(
                studentPins, minCredit, startDate, endDate
        );

        try {
            ByteArrayOutputStream excelData = generateExcelReport(reports);
            return Response.ok(excelData.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"student_course_report.xlsx\"")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    public ByteArrayOutputStream generateExcelReport(List<StudentCourseReport> reportData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Report");

        int rowNum = 1;
        String prevStudentPin = null;

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


}
