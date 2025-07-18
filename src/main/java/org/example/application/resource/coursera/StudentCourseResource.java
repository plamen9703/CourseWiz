package org.example.application.resource.coursera;

import org.example.application.api.coursera.StudentCourse;
import org.example.application.services.interfaces.coursera.StudentCourseService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("students-courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"course-admin", "instructor-admin"})
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
    @RolesAllowed({"instructor", "instructor-admin","course-admin"})
    public Response findById(@PathParam("pin") String studentPin, @PathParam("id") Integer courseId){
        StudentCourse studentCourse =new StudentCourse();
        studentCourse.setStudentPin(studentPin);
        studentCourse.setCourseId(courseId);
        StudentCourse found = studentCoursestudentCourseService.findById(studentCourse);
        return Response
                .ok(found)
                .build();
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<StudentCourse> studentCourses, @Context UriInfo uriInfo){
        List<URI> locations=new ArrayList<>();
        for (StudentCourse studentCourse: studentCourses){
            StudentCourse createdCourse = studentCoursestudentCourseService.create(studentCourse);
            URI location = uriInfo
                    .getAbsolutePathBuilder()
                    .path(createdCourse.getStudentPin())
                    .path(String.valueOf(createdCourse.getCourseId()))
                    .build();
            locations.add(location);
        }
        return Response.status(Response.Status.CREATED).entity(locations).build();
    }

    @POST
    @RolesAllowed({"course-admin", "instructor-admin","instructor"})
    public Response save(StudentCourse studentCourse, @Context UriInfo uriInfo){
        StudentCourse saved = studentCoursestudentCourseService.create(studentCourse);
        URI location =uriInfo.getAbsolutePathBuilder()
                .path(saved.getStudentPin()).path(String.valueOf(saved.getCourseId())).build();
        return Response.created(location).entity(saved).build();
    }

    @PUT // do I replace it with patch ????
    @RolesAllowed({"instructor","course-admin","instructor-admin"})
    public Response update(StudentCourse studentCourse){
        studentCoursestudentCourseService.update(studentCourse);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(StudentCourse studentCourse){
        studentCoursestudentCourseService.delete(studentCourse);
        return Response.noContent().build();
    }
}
