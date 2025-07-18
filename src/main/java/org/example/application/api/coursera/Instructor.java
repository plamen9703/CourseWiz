package org.example.application.api.coursera;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class Instructor  {
    private Integer id;
    @Size(max = 100, message = "First name must be at most 100 characters.")
    private String firstName;
    @Size(max = 100, message = "Last name must be at most 100 characters.")
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp timeCreated;

    public Instructor() {
    }

    public Instructor(Integer id, String firstName, String lastName, Timestamp timeCreated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.timeCreated = timeCreated;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", timeCreated=" + timeCreated +
                '}';
    }
}
