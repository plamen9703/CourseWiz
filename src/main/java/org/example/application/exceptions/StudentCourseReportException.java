package org.example.application.exceptions;

public class StudentCourseReportException extends RuntimeException {
    public StudentCourseReportException(String message) {
        super(message);
    }

    public StudentCourseReportException(String message, Exception e){
        super(message, e);
    }

}
