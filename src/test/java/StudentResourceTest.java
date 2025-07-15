//import org.example.application.services.auth.jwt.JwtAuthFilter;
//import org.example.application.resource.StudentResource;
//import org.example.application.resource.UserResource;
//import org.example.application.services.implementations.StudentServiceImpl;
//import org.example.application.services.implementations.UserServiceImpl;
//import org.example.application.services.interfaces.StudentService;
//import org.example.application.services.interfaces.UserService;
//import org.glassfish.hk2.utilities.binding.AbstractBinder;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.test.JerseyTest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Application;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class StudentResourceTest extends JerseyTest {
//
//    private String jwtToken;
//
//    @BeforeEach
//    public void loginAndGetToken() {
//        // Replace with your actual login endpoint and user credentials
//        Map<String, String> loginPayload = Map.of(
//                "userName", "admin",
//                "password", "admin123"
//        );
//
//        Response response = target("/users/login")
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(loginPayload));
//
//        assertEquals(200, response.getStatus());
//
//        Map<String, String> body = response.readEntity(Map.class);
//        assertTrue(body.containsKey("token"));
//
//        jwtToken = body.get("token");
//        assertNotNull(jwtToken);
//    }
//
//    @Test
//    public void testFindAllStudents_withValidToken() {
//        Response response = target("/students")
//                .request(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
//                .get();
//
//        assertEquals(200, response.getStatus());
//        // optionally assert body content
//    }
//
//    @Test
//    public void testFindAllStudents_withoutToken_shouldFail() {
//        Response response = target("/students")
//                .request(MediaType.APPLICATION_JSON)
//                .get();
//
//        assertEquals(401, response.getStatus());
//    }
//
//    @Override
//    protected Application configure() {
//        ResourceConfig config = new ResourceConfig(StudentResource.class)
//                .register(JwtAuthFilter.class)
//                .register(UserResource.class);
//
//        // Provide a mock or test instance of StudentService, for example with HK2 binding:
//        config.register(new AbstractBinder() {
//            @Override
//            protected void configure() {
//                bind(StudentServiceImpl.class).to(StudentService.class);
//            }
//        });
//        config.register(new AbstractBinder() {
//            @Override
//            protected void configure() {
//                bind(UserServiceImpl.class).to(UserService.class);
//            }
//        });
//        return config;
//    }
//}
