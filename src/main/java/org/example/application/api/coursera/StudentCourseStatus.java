package org.example.application.api.coursera;

import java.sql.Date;

public record StudentCourseStatus(String studentPin,Integer courseId, String firstName, String lastName, Date completionDate) {
}