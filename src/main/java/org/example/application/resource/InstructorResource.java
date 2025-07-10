package org.example.application.resource;

import org.example.application.api.Instructor;
import org.example.application.services.interfaces.InstructorService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@Path("instructors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InstructorResource {
    private  final InstructorService instructorService;

    public InstructorResource(InstructorService instructorService) {
        this.instructorService = instructorService;
    }


    @GET
    public Response findAll(){
        return Response.ok(instructorService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") int instructorId){
        return instructorService.findById(instructorId)
                .map( instructor -> Response.ok(instructor).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<Instructor> instructors){
        List<Instructor> created=new ArrayList<>();
        for (Instructor instructor: instructors){
            created.add(instructorService.create(instructor));
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    public Response create(Instructor instructor, @Context UriInfo uriInfo){
        Instructor saved=instructorService.create(instructor);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(instructor.getId())).build();
        return Response.created(location).entity(saved).build();
    }

    @PUT // do I replace it with patch ????
    public Response update(Instructor instructor){
        instructorService.update(instructor.getId(),instructor);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(Instructor instructor){
        instructorService.delete(instructor.getId());
        return Response.noContent().build();
    }

}
