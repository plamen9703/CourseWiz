package org.example.application.resource;

import org.example.application.api.User;
import org.example.application.jwt.NoAuth;
import org.example.application.services.interfaces.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Base64;
import java.util.Collections;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }
    @NoAuth
    @GET
    public Response getByIdentifier(@QueryParam("identifier") String identifier){
        System.out.println(identifier);
        User user = userService.findByUsernameorEmail(identifier)
                .orElseThrow(()->new NotFoundException("User with identifier %s not found."
                        .formatted(identifier)));
        return Response.ok(user).build();
    }

    @NoAuth
    @POST
    public Response createUser(User user, @Context UriInfo uriInfo){
        User saved=userService.create(user);
        URI location =uriInfo.getAbsolutePathBuilder()
                .path(saved.getUsername()).build();
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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials")
                    .build();
        }
    }
}
