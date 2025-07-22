package org.example.application.resource.users;


import org.example.application.api.users.UserInstructor;
import org.example.application.services.interfaces.users.UserInstructorService;
import org.example.application.services.jwt.JwtUtil;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

@Path("/api/user/instructor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserInstructorResource {

    private final UserInstructorService userInstructorService;


    public UserInstructorResource(UserInstructorService userInstructorService) {
        this.userInstructorService = userInstructorService;
    }


    @POST
    @Path("register")
    public Response registerInstructor(UserInstructor userInstructor){
        UserInstructor created=userInstructorService.create(userInstructor);
        String token = JwtUtil.generateToken(created);

        URI location = UriBuilder.fromResource(UserInstructorResource.class)
                .path(String.valueOf(created.getId()))
                .path("dashboard").build();

        return Response.created(location)
                .entity(Map.of("token", token, "user", created,"redirectedUrl",location))
                .build();
    }

    @POST
    @Path("login")
    @PermitAll
    public Response loginInstructor(UserInstructor userInstructor){
        String token = userInstructorService.login(userInstructor);
        Integer userId = userInstructorService.findByUsernameOrEmail(userInstructor).getId();
        URI location = UriBuilder
                .fromResource(UserInstructorResource.class)
                .path(String.valueOf(userId))
                .path("dashboard")
                .build();
        return Response.ok(Map.of("token", token)).location(location).build();
    }

    @Path("/{id}/dashboard")
    @GET
    @RolesAllowed({"instructor", "instructor-admin", "student-admin"})
    public Response getUserDashBoard(@PathParam("id") Integer id){
        UserInstructor userInstructor=new UserInstructor();
        userInstructor.setId(id);
        UserInstructor found = userInstructorService.findById(userInstructor);
        return Response.ok(found).build();
    }



}
