package org.example.application.api;

import java.security.Principal;
import java.sql.Timestamp;

public class AppUser extends User implements Principal {

    public AppUser(Integer id,String username, String email, String password, String role, Timestamp createdAt) {
        super(id, username, email, password, role, createdAt);
    }


    public AppUser(String username, String role) {
        setUsername(username);
        setPassword(role);
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
