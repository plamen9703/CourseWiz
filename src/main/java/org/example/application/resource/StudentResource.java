package org.example.application.resource;

import org.example.application.api.Student;
import org.example.application.services.interfaces.StudentService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@Path("students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"admin", "student-admin"})
public class StudentResource {

    private final StudentService studentService;


    public StudentResource(StudentService studentService) {
        this.studentService = studentService;
    }
    @GET
    public Response getAll(){
//        if @RolesAllowed not active
//        String userName = securityContext.getUserPrincipal().getName();
//        boolean isAdmin = securityContext.isUserInRole("admin");
        return Response.ok(studentService.findAll()).build();
    }

    @GET
    @Path("/{pin}")
    public Response findById(@PathParam("pin") String pin){
        return studentService.findById(pin)
                .map( student -> Response.ok(student).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/batch")
    public Response saveAll(List<Student> students){
        List<Student> created=new ArrayList<>();
        for (Student student: students){
            created.add(studentService.create(student));
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    public Response save(Student student, @Context UriInfo uriInfo){
        Student saved = studentService.create(student);
        URI location =uriInfo.getAbsolutePathBuilder()
                .path(saved.getPin()).build();
        return Response.created(location).entity(saved).build();
    }

    @PUT // do I replace it with patch ????
    public Response update(Student student){
        studentService.update(student.getPin(),student);
        return Response.noContent().build();
    }

    @DELETE
    public Response delete(Student student){
        studentService.delete(student.getPin());
        return Response.noContent().build();
    }

}
