package org.example.application.resource;

import org.example.application.api.Course;
import org.example.application.services.interfaces.CourseService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@Path("courses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"instructor-admin", "course-admin"})
public class CourseResource {
    private  final CourseService courseService;

    public CourseResource(CourseService courseService) {
        this.courseService = courseService;
    }


    @GET
    public Response getAll(){
        List<Course> courses = courseService.findAll();
        return Response.ok(courses).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"instructor-admin", "course-admin", "instructor"})
    public Response findById(@PathParam("id") int courseId){
        Course course = new Course();
        course.setId(courseId);
        Course found = courseService.findById(course);
        return Response
                .ok(found)
                .build();
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<Course> courses, @Context UriInfo uriInfo){
        List<URI> locations=new ArrayList<>();
        for (Course course: courses){
            Course saved = courseService.create(course);
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
    public Response create(Course course, @Context UriInfo uriInfo){
        Course saved= courseService.create(course);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(course.getId())).build();
        return Response.created(location).entity(saved).build();
    }

    @PUT // do I replace it with patch ????
    public Response update(Course course){
        courseService.update(course);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(Course course){
        courseService.delete(course);
        return Response.noContent().build();
    }
}
