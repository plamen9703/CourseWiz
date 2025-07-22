package org.example.application.api.coursera;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

public class Student  {
    @Pattern(regexp = "^STU\\d{7}$", message = "Pin must start with 'STU' and be followed by 7 digits.")
    private String pin;
    @Size(max = 50, message = "First name must be at most 50 characters.")
    private String firstName;
    @Size(max = 50, message = "Last name must be at most 50 characters.")
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Timestamp timeCreated;

    public Student(String pin, String firstName, String lastName, Timestamp timeCreated) {
        this.pin = pin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.timeCreated = timeCreated;
    }

    public Student() {
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {this.pin = pin;}

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
        return "Student{" +
                "pin='" + pin + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", timeCreated=" + timeCreated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(pin, student.pin) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(timeCreated, student.timeCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pin, firstName, lastName, timeCreated);
    }
}
