package org.example.application.api.users;

import org.example.application.api.coursera.Instructor;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class UserInstructor extends User{
    private Instructor instructor;

    public UserInstructor(Integer id, String username, String email, String password, Set<String> roles, Set<String> permissions, Timestamp createdAt, Instructor instructor) {
        super(id, username, email, password, roles, permissions, createdAt);
        this.instructor = instructor;
    }

    public UserInstructor(Instructor instructor) {
        super();
        this.instructor = instructor;
    }

    public UserInstructor(){
        super();
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserInstructor that = (UserInstructor) o;
        return Objects.equals(instructor, that.instructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), instructor);
    }
}
