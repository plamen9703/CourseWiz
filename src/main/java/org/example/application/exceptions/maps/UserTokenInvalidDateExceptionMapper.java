package org.example.application.exceptions.maps;

import org.example.application.exceptions.UserTokenInvalidDateException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UserTokenInvalidDateExceptionMapper implements ExceptionMapper<UserTokenInvalidDateException> {
    @Override
    public Response toResponse(UserTokenInvalidDateException exception) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("User Token invalid Date!", exception.getMessage()))
                .build();
    }
}
