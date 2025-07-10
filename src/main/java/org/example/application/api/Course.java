package org.example.application.api;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Course {
    private int id;
    private String name;
    private Integer instructorId;
    private short totalTime;
    private short credit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp timeCreated;

    public Course() {
    }

    public Course(int id, String name, Integer instructorId, short totalTime, short credit, Timestamp timeCreated) {
        this.id = id;
        this.name = name;
        this.instructorId = instructorId;
        this.totalTime = totalTime;
        this.credit = credit;
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public short getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(short totalTime) {
        this.totalTime = totalTime;
    }

    public short getCredit() {
        return credit;
    }

    public void setCredit(short credit) {
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
}
