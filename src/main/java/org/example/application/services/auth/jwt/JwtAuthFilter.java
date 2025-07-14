package org.example.application.services.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.example.application.api.User;

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
import java.util.HashSet;
import java.util.List;

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

            User principal=new User();

            String userName=claims.getSubject();
            principal.setUsername(userName);

            List<String> roles =claims.get("roles",List.class);
            HashSet<String> roleSet = new HashSet<>(roles);
            principal.setRoles(roleSet);

            List<String> permissions = claims.get("permissions", List.class);
            HashSet<String> permissionsSet = new HashSet<>(permissions);
            principal.setPermissions(permissionsSet);

            SecurityContext originalContext = containerRequestContext.getSecurityContext();
            JwtSecurityContext securityContext = new JwtSecurityContext(principal, originalContext.isSecure());
            containerRequestContext.setSecurityContext(securityContext);
        } catch (JwtException ex) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
