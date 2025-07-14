package org.example.application.services.auth.jwt;

import org.example.application.api.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Set;

public class JwtSecurityContext implements SecurityContext {

    private final User user;
    private final boolean isSecure;
    public JwtSecurityContext(User user, boolean isSecure) {
        this.user = user;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        Set<String> roles = user.getRoles();
        return  roles.contains(role);
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
