package org.example.application.exceptions.maps;

import org.example.application.exceptions.InvalidUserCredentialsException;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidUserCredentialsMapper  implements ExceptionMapper<InvalidUserCredentialsException> {
    @Override
    public Response toResponse(InvalidUserCredentialsException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Input error", e.getMessage())).build();
    }
}
