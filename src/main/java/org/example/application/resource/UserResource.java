package org.example.application.resource;

import org.example.application.api.User;
import org.example.application.services.auth.jwt.NoAuth;
import org.example.application.services.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Base64;
import java.util.Collections;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final static Logger LOGGER= LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }
    @NoAuth
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer userId){
        User user =new User();
        user.setId(userId);
        User found = userService.findById(user);
        return Response.ok(found).build();
    }

    @NoAuth
    @POST
    public Response createUser(@Valid User user, @Context UriInfo uriInfo){


        User saved=userService.create(user);
        URI location =uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(saved.getId())).build();
        return Response.created(location).build();
    }

//    @POST
//    @Path("/login")
//    public Response login(User user){
//
//        String token = userService.login(user);
//        return Response.ok(Collections.singletonMap("token", token)).build();
//    }

    @NoAuth
    @POST
    @Path("/login")
    public Response login(@Context HttpHeaders headers){
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        if(authHeader==null || !authHeader.startsWith("Basic ")){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing or invalid Authorization header")
                    .build();
        }

        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] parts = credentials.split(":",2);
        if(parts.length !=2){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid Basic Auth format")
                    .build();
        }
        String username= parts[0];
        String password= parts[1];
        User user =new User();
        user.setPassword(password);
        user.setUsername(username);
        try {
            String token = userService.login(user);
            return Response.ok(Collections.singletonMap("token", token)).build();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials")
                    .build();
        }
    }
}
