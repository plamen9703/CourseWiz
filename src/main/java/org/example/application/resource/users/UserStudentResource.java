package org.example.application.resource.users;

import io.dropwizard.auth.Auth;
import org.example.application.api.users.UserAuthenticated;
import org.example.application.api.users.UserStudent;
import org.example.application.services.interfaces.users.UserStudentService;
import org.example.application.services.jwt.JwtUtil;
import org.example.application.services.jwt.NoAuth;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

@Path("/api/user/student")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserStudentResource {

    private final UserStudentService userStudentService;

    public UserStudentResource(UserStudentService userStudentService) {
        this.userStudentService = userStudentService;
    }



    @NoAuth
    @Path("register")
    @POST
    @PermitAll
    public Response registerUserStudent(UserStudent userStudent){
        UserStudent created = userStudentService.create(userStudent);
        URI location = UriBuilder
                .fromResource(UserStudentResource.class)
                .path("/{id}/dashboard")
                .resolveTemplate("id", created.getId())
                .build();
        String token = JwtUtil.generateToken(created);
        return Response
                .created(location)
                .entity(Map.of(
                        "token",token,
                        "user", created,
                        "redirectedUrl","/dashboard"
                )).build();
    }

    @POST
    @Path("login")
    @PermitAll
    public Response loginUserStudent(UserStudent userStudent){
        try{
            String token = userStudentService.login(userStudent);
            Integer userId = userStudentService.findByUsernameOrEmail(userStudent).getId();
            URI location = UriBuilder.fromResource(UserStudentResource.class)
                    .path("/{id}/dashboard")
                    .resolveTemplate("id", userId)
                    .build();
            return Response.ok(Map.of("token", token)).location(location).build();
        }catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials!")
                    .build();
        }
    }

    @GET
    @Path("/{id}/dashboard")
    @RolesAllowed({"student", "instructor", "student-admin", "course-admin"})
    public Response getUserStudentDashboard(@PathParam("id") Integer userId){
        UserStudent toFind= new UserStudent();
        toFind.setId(userId);
        UserStudent foundUserStudent= userStudentService.findById(toFind);

        return Response.ok(foundUserStudent).build();
    }


    @Path("/{id}")
    @PUT
    @RolesAllowed({"student", "student-admin"})
    public Response editsUserInfo(@Auth UserAuthenticated userAuthenticated,
                                  @PathParam("id") Integer id,
                                  UserStudent user){

        if(!id.equals(user.getId())) {
            String message;
            message = String.format("User id can not be different that request id. User id: %d, Request id: %d", user.getId(), id);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(
                            Map.of("error","Invalid request!",
                                    "message", message))
                    .build();
        }
        if(!id.equals(userAuthenticated.getId())) {
            String message = "You don't have permission to change this user data!";
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error","Unauthorized access.","message", message)).build();
        }

        userStudentService.update(user);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
