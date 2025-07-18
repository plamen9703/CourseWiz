package org.example.application.exceptions.maps;

import org.example.application.exceptions.UserTokenInvalidException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UserTokenExceptionMapper implements ExceptionMapper<UserTokenInvalidException> {
    @Override
    public Response toResponse(UserTokenInvalidException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Token error.", exception.getMessage())).build();
    }
}
