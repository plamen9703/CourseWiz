package org.example.application.resource.coursera;

import org.example.application.api.coursera.Student;
import org.example.application.services.interfaces.coursera.StudentService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@Path("students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"student-admin"})
public class StudentResource {

    private final StudentService studentService;


    public StudentResource(StudentService studentService) {
        this.studentService = studentService;
    }
    @GET
    @RolesAllowed({"instructor", "course-admin"})
    public Response findAll(){
//        Needed @Contex SecurityPrincipal securityprincipal in method args
//        User user=(User)securityContext.getUserPrincipal();
//        if @RolesAllowed not active
//        String userName = securityContext.getUserPrincipal().getName();
//        boolean isAdmin = securityContext.isUserInRole("admin");

        List<Student> students = studentService.findAll();
        return Response.ok(students).build();
    }

    @GET
    @Path("/{pin}")
    @RolesAllowed({"student-admin","instructor","instructor-admin"})
    public Response findById(@PathParam("pin") String pin){
        Student student=new Student();
        student.setPin(pin);
        Student found = studentService.findById(student);
        return Response
                .ok(found)
                .build();
    }

    @POST
    @Path("/batch")
    @RolesAllowed({"student-admin", "course-admin", "instructor"})
    public Response saveAll(List<Student> students, @Context UriInfo uriInfo){
        List<URI> locations=new ArrayList<>();
        for (Student student: students){
            Student created = studentService.create(student);
            URI location=UriBuilder.fromResource(StudentResource.class).path(created.getPin()).build();
            locations.add(location);
        }
        return Response
                .status(Response.Status.CREATED)
                .entity(locations)
                .build();
    }

    @POST
    @RolesAllowed({"student-admin","course-admin", "instructor-admin"})
    public Response save(Student student, @Context UriInfo uriInfo){
        Student created = studentService.create(student);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(created.getPin())
                .build();
        return Response.created(location).build();
    }

    @PUT // do I replace it with patch ????
    @RolesAllowed({"student-admin", "student"})
    public Response update(Student student){
        studentService.update(student);
        return Response.noContent().build();
    }

    @DELETE
    @RolesAllowed({"student-admin","course-admin", "instructor-admin"})
    public Response delete(Student student){
        studentService.delete(student);
        return Response.noContent().build();
    }

}
