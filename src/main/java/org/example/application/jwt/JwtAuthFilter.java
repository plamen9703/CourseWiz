package org.example.application.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    private final SecretKey secretKey;

    @Inject
    private ResourceInfo resourceInfo;

    public JwtAuthFilter(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(NoAuth.class))return;

        String authHeader= containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        String token = authHeader.substring("Bearer ".length());

        try {
            Claims claims= Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String userName=claims.getSubject();
            String role = claims.get("role",String.class);
            SecurityContext originalContext = containerRequestContext.getSecurityContext();
            JwtSecurityContext securityContext = new JwtSecurityContext(userName, role, originalContext.isSecure());
            containerRequestContext.setSecurityContext(securityContext);
        } catch (JwtException ex) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
