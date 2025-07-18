package org.example.application.resource.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.application.services.interfaces.coursera.InstructorService;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"instructor-admin", "course-admin"})
public class InstructorResource {
    private  final InstructorService instructorService;

    public InstructorResource(InstructorService instructorService) {
        this.instructorService = instructorService;
    }


    @GET
    public Response findAll(){
        List<Instructor> instructors = instructorService.findAll();
        return Response.ok(instructors).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"instructor-admin", "course-admin","instructor"})
    public Response findById(@PathParam("id") Integer id){
        Instructor instructor=new Instructor();
        instructor.setId(id);
        Instructor found= instructorService.findById(instructor);
        return Response
                .ok(found)
                .build();
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<Instructor> instructors, @Context UriInfo uriInfo){
        List<URI> locations=new ArrayList<>();
        for (Instructor instructor: instructors){
            Instructor saved = instructorService.create(instructor);
            URI build = uriInfo.getAbsolutePathBuilder()
                    .path(String.valueOf(saved.getId()))
                    .build();
            locations.add(build);
        }
        return Response.status(Response.Status.CREATED)
                .entity(locations)
                .build();
    }

    @POST
    public Response create(Instructor instructor, @Context UriInfo uriInfo){
        instructorService.create(instructor);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(instructor.getId())).build();
        return Response.created(location).build();
    }

    @PUT // do I replace it with patch ????
    @RolesAllowed({"instructor-admin", "course-admin","instructor"})
    public Response update(Instructor instructor){
        instructorService.update(instructor);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(Instructor instructor){
        instructorService.delete(instructor);
        return Response.noContent().build();
    }

}
