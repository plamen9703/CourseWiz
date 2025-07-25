package org.example.application.api.coursera;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.Objects;

public class Course {
    private Integer id;
    private String name;
    private Integer instructorId;
    private Short totalTime;
    private Short credit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp timeCreated;

    public Course() {
    }

    public Course(Integer id, String name, Integer instructorId, Short totalTime, Short credit, Timestamp timeCreated) {
        this.id = id;
        this.name = name;
        this.instructorId = instructorId;
        this.totalTime = totalTime;
        this.credit = credit;
        this.timeCreated = timeCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
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

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instructor=" + instructorId +
                ", totalTime=" + totalTime +
                ", credit=" + credit +
                ", timeCreated=" + timeCreated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(instructorId, course.instructorId) && Objects.equals(totalTime, course.totalTime) && Objects.equals(credit, course.credit) && Objects.equals(timeCreated, course.timeCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, instructorId, totalTime, credit, timeCreated);
    }
}
