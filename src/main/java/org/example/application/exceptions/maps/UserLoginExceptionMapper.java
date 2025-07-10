package org.example.application.exceptions.maps;

import org.example.application.exceptions.UserLoginException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UserLoginExceptionMapper implements ExceptionMapper<UserLoginException> {
    @Override
    public Response toResponse(UserLoginException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("Log in failed", e.getMessage()))
                .build();
    }
}
