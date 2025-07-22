package org.example.application.api.coursera;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourse that = (StudentCourse) o;
        return Objects.equals(studentPin, that.studentPin) && Objects.equals(courseId, that.courseId) && Objects.equals(completionDate, that.completionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentPin, courseId, completionDate);
    }
}
