package org.example.application.resource;

import org.example.application.api.Course;
import org.example.application.services.interfaces.CourseService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
@Path("courses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
    private  final CourseService courseService;

    public CourseResource(CourseService courseService) {
        this.courseService = courseService;
    }


    @GET
    public Response getAll(){
        return Response.ok(courseService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") int courseId){
        return courseService.findById(courseId)
                .map( course -> Response.ok(course).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<Course> courses){
        for (Course course: courses){
            courseService.create(course);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    public Response create(Course course, @Context UriInfo uriInfo){
        Course saved= courseService.create(course);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(course.getId())).build();
        return Response.created(location).entity(saved).build();
    }

    @PUT // do I replace it with patch ????
    public Response update(Course course){
        courseService.update(course.getId(),course);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(Course course){
        courseService.delete(course.getId());
        return Response.noContent().build();
    }

}
