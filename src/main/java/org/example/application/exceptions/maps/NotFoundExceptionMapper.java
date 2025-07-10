package org.example.application.exceptions.maps;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NoContentException> {
    @Override
    public Response toResponse(NoContentException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Not Found", e.getMessage())).build();
    }
}
