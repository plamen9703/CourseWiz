package org.example.application.exceptions.maps;

import org.example.application.exceptions.StudentCourseReportException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class StudentCourseReportExceptionMapper implements ExceptionMapper<StudentCourseReportException> {

    @Override
    public Response toResponse(StudentCourseReportException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Report Error!", e.getMessage())).build();
    }
}
