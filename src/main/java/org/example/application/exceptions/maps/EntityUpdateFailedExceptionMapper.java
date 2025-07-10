package org.example.application.exceptions.maps;

import org.example.application.exceptions.DuplicateEntityException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EntityUpdateFailedExceptionMapper implements ExceptionMapper<DuplicateEntityException> {
    @Override
    public Response toResponse(DuplicateEntityException e) {
        return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse("Conflict", e.getMessage())).build();
    }
}
