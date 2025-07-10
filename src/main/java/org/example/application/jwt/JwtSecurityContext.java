package org.example.application.jwt;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JwtSecurityContext implements SecurityContext {

    private final String userName;
    private final String role;
    private final boolean isSecure;

    public JwtSecurityContext(String userName, String role, boolean isSecure) {
        this.userName = userName;
        this.role = role;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return ()->userName;
    }

    @Override
    public boolean isUserInRole(String role) {
        return this.role!=null && this.role.equals(role);
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
