package org.example.application.api.users;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;

public class UserAuthenticated implements Principal {
    private final Integer id;
    private final String username;
    private final String email;
    private final Set<String> roles;
    private final Set<String> permissions;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthenticated that = (UserAuthenticated) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(roles, that.roles) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, roles, permissions);
    }

    @Override
    public String toString() {
        return "UserAuthenticated{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }

    public UserAuthenticated(Integer id, String username, String email, Set<String> roles, Set<String> permissions) {
        this.id=id;
        this.email=email;
        this.username=username;
        this.permissions=permissions;
        this.roles=roles;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
