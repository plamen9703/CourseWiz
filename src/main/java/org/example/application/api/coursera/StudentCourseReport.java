package org.example.application.api.coursera;

public class StudentCourseReport {
    private String studentPin;
    private String StudentName;
    private Integer totalCredit;
    private Integer courseId;
    private String courseName;
    private Short totalTime;
    private Short credit;
    private String instructorName;

    public StudentCourseReport(String studentPin, String studentName, Integer totalCredit, Integer courseId, String courseName, Short totalTime, Short credit, String instructorName) {
        this.studentPin = studentPin;
        StudentName = studentName;
        this.totalCredit = totalCredit;
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalTime = totalTime;
        this.credit = credit;
        this.instructorName = instructorName;
    }

    public StudentCourseReport() {
    }

    public String getStudentPin() {
        return studentPin;
    }

    public void setStudentPin(String studentPin) {
        this.studentPin = studentPin;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public Integer getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Short getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Short totalTime) {
        this.totalTime = totalTime;
    }

    public Short getCredit() {
        return credit;
    }

    public void setCredit(Short credit) {
        this.credit = credit;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    @Override
    public String toString() {
        return "StudentCourseReport{" +
                "studentPin='" + studentPin + '\'' +
                ", StudentName='" + StudentName + '\'' +
                ", totalCredit=" + totalCredit +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", totalTime=" + totalTime +
                ", credit=" + credit +
                ", instructorName='" + instructorName + '\'' +
                '}';
    }
}
