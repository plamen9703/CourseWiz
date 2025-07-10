package org.example.application.exceptions.maps;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidUserCredentialsMapper  implements ExceptionMapper<NoContentException> {
    @Override
    public Response toResponse(NoContentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Input error", e.getMessage())).build();
    }
}
