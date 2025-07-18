package org.example.application.api.coursera;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class StudentCourse {
    private String studentPin;
    private Integer courseId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date completionDate;

    public StudentCourse() {
    }

    public StudentCourse(String studentPin, Integer courseId, Date completionDate) {
        this.studentPin = studentPin;
        this.courseId = courseId;
        this.completionDate = completionDate;
    }

    public String getStudentPin() {
        return studentPin;
    }

    public void setStudentPin(String studentPin) {
        this.studentPin = studentPin;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
}
