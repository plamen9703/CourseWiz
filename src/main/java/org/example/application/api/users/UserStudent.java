package org.example.application.api.users;

import org.example.application.api.coursera.Student;

import java.util.Objects;
import java.util.Set;

public class UserStudent extends User{

    private Student student;

    public UserStudent(Integer id, String username, String email, String password, Set<String> roles, Set<String> permissions) {
        super(id, username, email, password, roles, permissions);
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
    public String toString() {
        return super.toString()+"UserStudent{" +
                "student=" + student +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStudent)) return false;
        if (!super.equals(o)) return false;
        UserStudent that = (UserStudent) o;
        return Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), student);
    }
}
