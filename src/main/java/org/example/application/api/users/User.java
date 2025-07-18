package org.example.application.api.users;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public  class User {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles =new HashSet<>();
    private Set<String> permissions=new HashSet<>();
    private Timestamp createdAt;
//    @Valid
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private Profile profile;

    public User(Integer id, String username, String email, String password, Set<String> roles, Set<String> permissions, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.createdAt = createdAt;
    }

    public User() {
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdentifier() {
        return this.username == null ? this.email : this.username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + roles + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }


}
