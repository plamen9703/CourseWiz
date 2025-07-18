package org.example.application.services.jwt.authorization;

import io.dropwizard.auth.AuthorizationContext;
import io.dropwizard.auth.Authorizer;
import org.example.application.api.users.UserAuthenticated;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;

public class RoleAuthorizer  implements Authorizer<UserAuthenticated> {

    @Override
    public boolean authorize(UserAuthenticated user, String role) {
        return user.getRoles().contains(role);
    }

    @Override
    public boolean authorize(UserAuthenticated principal, String role, @Nullable ContainerRequestContext requestContext) {
//        return principal.getRoles().contains(role);
        return Authorizer.super.authorize(principal, role, requestContext);
    }

    @Override
    public AuthorizationContext<UserAuthenticated> getAuthorizationContext(UserAuthenticated principal, String role, @Nullable ContainerRequestContext requestContext) {
        return Authorizer.super.getAuthorizationContext(principal, role, requestContext);
    }


//    @Override
//    public boolean authorize(User user, String role) {
//        if(user == null || user.getRoles() == null)
//            return false;
//        return user.getRoles().contains(role);
//    }

//    @Override
//    public boolean authorize(User principal, String role, @Nullable ContainerRequestContext requestContext) {
//        return Authorizer.super.authorize(principal, role, requestContext);
//    }
//
//    @Override
//    public AuthorizationContext<User> getAuthorizationContext(User principal, String role, @Nullable ContainerRequestContext requestContext) {
//        return Authorizer.super.getAuthorizationContext(principal, role, requestContext);
//    }
}
