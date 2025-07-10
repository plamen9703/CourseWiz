package org.example.application.exceptions.maps;

import org.example.application.exceptions.EntityUpdateFailedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EntityInserFailedExceptionMapper implements ExceptionMapper<EntityUpdateFailedException> {

    @Override
    public Response toResponse(EntityUpdateFailedException entityUpdateFailedException) {
        return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse("Conflict",entityUpdateFailedException.getMessage())).build();
    }
}
