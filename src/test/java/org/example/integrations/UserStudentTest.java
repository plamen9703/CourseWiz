package org.example.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.example.CoursewizApplication;
import org.example.CoursewizConfiguration;
import org.example.application.api.coursera.Student;
import org.example.application.api.users.User;
import org.example.application.api.users.UserStudent;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserStudentTest {

    private static final Logger log = LoggerFactory.getLogger(UserStudentTest.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();


    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15-alpine")  // <-- alpine (not alphine)
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    static {
        POSTGRES.start();
    }

    @RegisterExtension
    public static final DropwizardAppExtension<CoursewizConfiguration> APP = new DropwizardAppExtension<>(
            CoursewizApplication.class,
            ResourceHelpers.resourceFilePath("test-config.yml"),
            ConfigOverride.config("database.url", POSTGRES.getJdbcUrl()),
            ConfigOverride.config("database.user", POSTGRES.getUsername()),
            ConfigOverride.config("database.password", POSTGRES.getPassword())
    );


    // Remove this from test:
    @BeforeAll
    static void migrate() throws Exception {
        Flyway flyway = Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .locations("classpath:db/migrations", "classpath:db/seeds")
                .cleanDisabled(false)
                .sqlMigrationPrefix("S")
                .load();

        // Run migrations
        flyway.clean();
        flyway.migrate();

        APP.before();
        client=APP.client();
        localport=APP.getLocalPort();
    }

    @AfterAll
    static void close() {
        POSTGRES.stop();
        APP.after();
    }


//    @Test
//    void seralizesToJSON() throws Exception {
//        final UserStudent userStudent = new UserStudent();
//        userStudent.setUsername("Alice");
//        userStudent.setEmail("alice@example.com");
//        userStudent.setPassword("alice123");
//        userStudent.setStudent(new Student(null, "Alice", "Alison", null));
//
//        final String expected = MAPPER.writeValueAsString(
//                MAPPER.readValue(getClass().getResource("/fixtures/user_students.json"), JsonNode.class));
//        assertThat(MAPPER.writeValueAsString(userStudent)).isEqualTo(expected);
//
//    }
    static URI createdUserLocation;
    static String userJwtToken;

    static Client client;
    static Integer localport;

    static Integer newUserId;

    @Test
    @Order(1)
    void registerUserStudent() throws IOException {
        List<UserStudent> userStudents = MAPPER.readValue(getClass().getResource("/fixtures/user_students.json"), new TypeReference<List<UserStudent>>() {});
        User target=userStudents.stream()
                .filter(userStudent -> userStudent.getUsername().equals("plamen"))
                .findFirst()
                .orElseThrow();

        try (Response response = client
                .target(
                        String.format("http://localhost:%d/api/user/student/register", localport)
                )
                .request()
                .post(Entity.json(target))) {
            assertThat(response.getStatus()).isEqualTo(201);
            Map<String , Object> map = response.readEntity(new GenericType<Map<String, Object>>(){});
            createdUserLocation=response.getLocation();
            UserStudent userStudent = MAPPER.convertValue(map.get("user"), UserStudent.class);
            newUserId = userStudent.getId();
        }
    }

    @Test
    @Order(2)
    void loginUserStudent() {
        User user = new User();
        user.setUsername("plamen");
        user.setPassword("plamen456");


        Map<String, String> entity;
        try (Response response = client
                .target(String.format("http://localhost:%d/api/user/student/login", localport))
                .request()
                .post(Entity.json(user))) {

            assertEquals(200, response.getStatus());
            entity = response.readEntity(new GenericType<Map<String, String>>() {
            });
        }
        String token = entity.get("token");
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
        userJwtToken=token;
    }

    @Test
    @Order(3)
    void seeUserStudentProfile() {
        UserStudent expected = new UserStudent();
        expected.setId(1);
        expected.setUsername("plamen");
        expected.setEmail("hristov@gmail.com");
        expected.setRoles(new HashSet<>(List.of("student")));
        expected.setStudent(new Student("STU0000011", "Plamen", "Hristov", null));
        Response response= client.target(createdUserLocation)
                .request()
                .header("Authorization", "Bearer "+userJwtToken)
                .get();
        assertEquals(200, response.getStatus());
        UserStudent entity = response.readEntity(new GenericType<UserStudent>(){});
        entity.getStudent().setTimeCreated(null);
        assertEquals(expected, entity);
    }

    @Test
    @Order(4)
    void changeUserStudentLoginWhenUserBodyIsEmpty() {
        UserStudent userStudent=new UserStudent();
        try (Response response = client
                .target(String.format("http://localhost:%d/api/user/student/%d", localport, newUserId))
                .request()
                .header("Authorization", "Bearer " + userJwtToken)
                .put(Entity.json(userStudent))) {
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        }catch (Exception e){
            log.error(e, e::getMessage);
        }
    }

    @Test
    @Order(5)
    void changeUserStudentLoginWheUserIsNotAuthorized(){
        String otherUserJwt= prepareToken();

        User user = new User();
        user.setId(1);
        String url="http://localhost:%d/api/user/student/%d".formatted(localport, newUserId);
        try (Response response = client
                .target(url)
                .request()
                .header("Authorization","Bearer "+otherUserJwt)
                .put(Entity.json(user))) {
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),response.getStatus());
            Map<String, String> map= response.readEntity(new GenericType<Map<String, String>>(){});
            assertEquals("Unauthorized access.", map.get("error"));
            assertEquals("You don't have permission to change this user data!", map.get("message"));
        }

    }

    private String prepareToken() {
        try {
            // Load user from JSON fixture
            TypeReference<List<UserStudent>> valueTypeRef = new TypeReference<>() {};
            UserStudent userStudent = MAPPER
                    .readValue(getClass().getResource("/fixtures/user_students.json"), valueTypeRef)
                    .stream()
                    .filter(u -> u.getUsername().equals("Alice"))
                    .findFirst()
                    .orElseThrow();
            String url = "http://localhost:%d/api/user/student/register".formatted(localport);

            try (Response response = client.target(url)
                    .request()
                    .post(Entity.json(userStudent))) {

                if (response.getStatus() != 201) {
                    throw new RuntimeException("Registration failed, status: " + response.getStatus());
                }

                String json = response.readEntity(String.class);
                Map<String, Object> map = MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
                return (String) map.get("token");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(6)
    void updateWhenUserStudentIsAuthorized(){
        User user=new User();
        user.setId(1);
        user.setEmail("papa234@gmail.com");
        user.setPassword("papa123");
        try (Response response = client
                .target("http://localhost:%d/api/user/student/%d".formatted(localport, newUserId))
                .request()
                .header("Authorization", "Bearer " + userJwtToken)
                .put(Entity.json(user));
        Response updateUserReponse =client
                .target("http://localhost:%d/api/user/student/%d/dashboard".formatted(localport,newUserId))
                .request()
                .header("Authorization", "Bearer "+userJwtToken)
                .get()) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            assertEquals(Response.Status.OK.getStatusCode(), updateUserReponse.getStatus());
            UserStudent userStudent = updateUserReponse.readEntity(new GenericType<UserStudent>() {});
            assertEquals("papa234@gmail.com", userStudent.getEmail());
        }
    }

}
