package org.example.application.services.jwt;

import org.example.application.api.users.UserAuthenticated;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Set;

public class JwtSecurityContext implements SecurityContext {

    private final UserAuthenticated user;
    private final boolean isSecure;
    private Set<String> roles;
    public JwtSecurityContext(UserAuthenticated user, boolean isSecure) {
        this.user = user;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return  user.getRoles() != null && user.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
