package org.example.application.resource;

import org.example.application.api.StudentCourse;
import org.example.application.services.interfaces.StudentCourseService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("students-courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentCourseResource {

    private final StudentCourseService studentCoursestudentCourseService;


    public StudentCourseResource(StudentCourseService studentCoursestudentCourseService) {
        this.studentCoursestudentCourseService = studentCoursestudentCourseService;
    }
    @GET
    public Response getAll(){
        return Response.ok(studentCoursestudentCourseService.findAll()).build();
    }

    @GET
    @Path("/{pin}/{id}")
    public Response findById(@PathParam("pin") String studentPin, @PathParam("id") Integer courseId){
        return studentCoursestudentCourseService.findById(studentPin,courseId)
                .map( studentCourse -> Response.ok(studentCourse).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<StudentCourse> studentCourses){
        for (StudentCourse studentCourse: studentCourses){
            studentCoursestudentCourseService.create(studentCourse);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    public Response save(StudentCourse studentCourse, @Context UriInfo uriInfo){
        StudentCourse saved = studentCoursestudentCourseService.create(studentCourse);
        URI location =uriInfo.getAbsolutePathBuilder()
                .path(saved.getStudentPin()).path(String.valueOf(saved.getCourseId())).build();
        return Response.created(location).entity(saved).build();
    }

    @PUT // do I replace it with patch ????
    public Response update(StudentCourse studentCourse){
        studentCoursestudentCourseService.update(studentCourse.getStudentPin(),studentCourse.getCourseId(),studentCourse);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(StudentCourse studentCourse){
        studentCoursestudentCourseService.delete(studentCourse.getStudentPin(),studentCourse.getCourseId());
        return Response.noContent().build();
    }
}
