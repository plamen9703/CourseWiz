package org.example.application.api.users;

import org.example.application.api.coursera.Student;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class UserStudent extends User{

    private Student student;

    public UserStudent(Integer id, String username, String email, String password, Set<String> roles, Set<String> permissions, Timestamp createdAt) {
        super(id, username, email, password, roles, permissions, createdAt);
    }

    public UserStudent(){
        super();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserStudent that = (UserStudent) o;
        return Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), student);
    }
}
