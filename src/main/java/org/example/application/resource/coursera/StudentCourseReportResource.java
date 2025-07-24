package org.example.application.resource.coursera;

import org.example.application.api.coursera.StudentCourseReport;
import org.example.application.exceptions.StudentCourseReportException;
import org.example.application.services.interfaces.coursera.StudentCourseReportService;

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
@RolesAllowed({"course-admin", "instructor-admin","student-admin", "instructor"})
public class StudentCourseReportResource {
    private final StudentCourseReportService studentCourseReportService;


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
        // -- Excel Export Logic --

        List<StudentCourseReport> reports = studentCourseReportService.getStudentCourseReports(
                studentPins, minCredit, startDate, endDate
        );
        try {
            ByteArrayOutputStream excelData = studentCourseReportService.generateExcelReport(reports);
            return Response.ok(excelData.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"student_course_report.xlsx\"")
                    .build();
        } catch (IOException e) {
            throw new StudentCourseReportException("Failed to generate report!", e);
        }
    }




}
