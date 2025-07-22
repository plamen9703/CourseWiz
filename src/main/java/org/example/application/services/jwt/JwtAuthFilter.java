package org.example.application.services.jwt;

import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import org.example.application.api.users.UserAuthenticated;
import org.example.application.exceptions.UserTokenInvalidException;
import org.example.application.services.jwt.authentication.JwtAuthenticator;
import org.example.application.services.jwt.authorization.RoleAuthorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter extends AuthFilter<String, UserAuthenticated> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

    private  final JwtAuthenticator JWT_AUTHENTICATOR ;
    private  final RoleAuthorizer ROLE_AUTHORIZER ;

    public JwtAuthFilter(JwtAuthenticator jwtAuthenticator, RoleAuthorizer roleAuthorizer) {
        JWT_AUTHENTICATOR = jwtAuthenticator;
        ROLE_AUTHORIZER = roleAuthorizer;
    }

    @Inject
    ResourceInfo resourceInfo;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        boolean hasAuthAnnotation = hashAuthAnotation(); // your method checking for @Auth

// Skip if NoAuth or PermitAll
        if (resourceInfo.getResourceMethod().isAnnotationPresent(NoAuth.class)
            || resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            return;
        }

// Deny if DenyAll
        if (resourceInfo.getResourceMethod().isAnnotationPresent(DenyAll.class)) {
            containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("message", "Method not authorized!"))
                    .build());
            return;
        }

// If method has @Auth OR @RolesAllowed, we require JWT
        boolean requiresAuth = hasAuthAnnotation
                               || resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class);

        if (!requiresAuth) {
            return; // No auth required
        }

// Validate token
        String authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Missing or invalid Authorization header"))
                    .build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        UserAuthenticated userAuthenticated;
        try {
            userAuthenticated = JWT_AUTHENTICATOR.authenticate(token)
                    .orElseThrow(() -> new UserTokenInvalidException("User token is invalid!"));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        boolean secure = containerRequestContext.getSecurityContext().isSecure();
// Set security context
        containerRequestContext.setSecurityContext(new JwtSecurityContext(userAuthenticated, secure));

// If @RolesAllowed present, check roles
        RolesAllowed rolesAllowed = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);
        if (rolesAllowed == null) {
            rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
        }
        if (rolesAllowed != null) {
            boolean authorized = Arrays.stream(rolesAllowed.value())
                    .anyMatch(role -> ROLE_AUTHORIZER.authorize(userAuthenticated, role));
            if (!authorized) {
                containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("You do not have permission to access this resource!")
                        .build());
            }
        }

//            UserAuthenticated userAuthenticated = JWT_AUTHENTICATOR.authenticate(token).orElseThrow();
//            for(String role: principal.getRoles()){
//                if(ROLE_AUTHORIZER.authorize(userAuthenticated,role))
//                    securityContext.se
//            }

        //            Claims claims= Jwts.parser()
//                    .verifyWith(JwtUtil.getKey())
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//
//            UserAuthenticated principal=new UserAuthenticated();
//
//            String userName=claims.getSubject();
//            principal.setUsername(userName);
//
//            List<String> roles =claims.get("roles",List.class);
//            HashSet<String> roleSet = new HashSet<>(roles);
//            principal.setRoles(roleSet);
//
//            List<String> permissions = claims.get("permissions", List.class);
//            HashSet<String> permissionsSet = new HashSet<>(permissions);
//            principal.setPermissions(permissionsSet);

//            SecurityContext jwtSecurityContext =new  JwtSecurityContext(userAuthenticated, isSecure);
//            System.out.println(jwtSecurityContext.isSecure());
    }

    private boolean hashAuthAnotation() {
        return Arrays.stream(resourceInfo.getResourceMethod().getParameters()).anyMatch(p->p.isAnnotationPresent(Auth.class));
    }

}
