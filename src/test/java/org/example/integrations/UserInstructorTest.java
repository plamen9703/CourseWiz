package org.example.integrations;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.CoursewizApplication;
import org.example.CoursewizConfiguration;
import org.example.application.api.coursera.Student;
import org.example.application.api.users.User;
import org.example.application.api.users.UserInstructor;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserInstructorTest {


    private static final ObjectMapper MAPPER=new ObjectMapper();

    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");
    static {
        POSTGRES.start();
    }
    @RegisterExtension
    public final static DropwizardAppExtension<CoursewizConfiguration> APP=
            new DropwizardAppExtension<>(
                    CoursewizApplication.class,
                    ResourceHelpers.resourceFilePath("test-config.yml"),
                    ConfigOverride.config("database.url", POSTGRES.getJdbcUrl()),
                    ConfigOverride.config("database.user", POSTGRES.getUsername()),
                    ConfigOverride.config("database.password", POSTGRES.getPassword())
            );
    private static final Logger log = LoggerFactory.getLogger(UserInstructorTest.class);



    static Client client;
    static Integer localPort;

    @BeforeAll
    static void setup() throws Exception {
        Flyway flyway=Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .locations("classpath:db/migrations", "classpath:db/seeds")
                .cleanDisabled(false)
                .sqlMigrationPrefix("S")
                .load();

        flyway.clean();
        flyway.migrate();
        APP.before();
        client= APP.client();
        localPort=APP.getLocalPort();
    }

    @AfterAll
    static void after(){
        POSTGRES.stop();
        APP.after();
    }

    static URI newUserLocation;
    static Integer newUserId;
    static String userJwtToken;

    @Test
    @Order(1)
    void registerUserInstructor(){
        try {
            UserInstructor userInstructor = MAPPER
                    .readValue(getClass().getResource("/fixtures/user_instructors.json"), new TypeReference<List<UserInstructor>>() {})
                    .stream()
                    .filter(u->u.getUsername().contains("m"))
                    .findFirst()
                    .orElseThrow();
            try (Response response = client
                    .target("http://localhost:%d/api/user/instructor/register".formatted(localPort))
                    .request()
                    .post(Entity.json(userInstructor))) {
                assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
                newUserLocation=response.getLocation();
                Map<String, Object> map=response.readEntity(new GenericType<Map<String, Object>>(){});
                UserInstructor createdUser=MAPPER.convertValue(map.get("user"), UserInstructor.class);
                assertNull(createdUser.getPassword());
                assertEquals("momchil@example.com", createdUser.getEmail());
                assertNotNull(createdUser.getId());
                newUserId=createdUser.getId();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    @Order(2)
    void loginUserInstructorWithEmail(){
        User user=new User();
        user.setEmail("momchil@example.com");
        user.setPassword("MomchilABV");
        try (Response response = client
                .target("http://localhost:%d/api/user/instructor/login".formatted(localPort))
                .request()
                .post(Entity.json(user))) {
            Map<String, String> map=response.readEntity(new GenericType<Map<String, String>>(){});
            String token = MAPPER.convertValue(map.get("token"), String.class);

            assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
            assertNotNull(token);
            assertTrue(token.startsWith("ey"));
            userJwtToken=token;
        }

    }

    @Test
    @Order(3)
    void loginUserInstructorWithUsername(){
        User user=new User();
        user.setUsername("momchil");
        user.setPassword("MomchilABV");
        try (Response response = client
                .target("http://localhost:%d/api/user/instructor/login".formatted(localPort))
                .request()
                .post(Entity.json(user))) {
            Map<String, String> map=response.readEntity(new GenericType<Map<String, String>>(){});
            String token = MAPPER.convertValue(map.get("token"), String.class);

            assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
            assertNotNull(token);
            assertTrue(token.startsWith("ey"));
            userJwtToken=token;
        }
    }


    @Test
    @Order(4)
    void getUserInstructorProfile(){
        Response response=client
                .target(newUserLocation)
                .request()
                .header("Authorization", "Bearer "+userJwtToken)
                .get();
        log.info(newUserLocation.toString());
        UserInstructor userInstructor =response.readEntity(new GenericType<UserInstructor>(){});
        assertNull(userInstructor.getPassword());
        assertEquals("momchil", userInstructor.getUsername());
        assertEquals("momchil@example.com", userInstructor.getEmail());
        assertEquals(6, userInstructor.getInstructor().getId());
        assertEquals("Momchil", userInstructor.getInstructor().getFirstName());
        assertEquals("Ivanov", userInstructor.getInstructor().getLastName());

    }


    @Test
    @Order(5)
    void shouldGenerateExcelReport() throws Exception {
        // Prepare token for auth
        String token = userJwtToken; // your helper method to log in and get JWT

        Response response = client.target("http://localhost:" + localPort + "/reports/student-course")
//                .queryParam("students", "12345", "67890")
//                .queryParam("minCredit", 3)
                .queryParam("minCredit",6)
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .accept("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaderString("Content-Disposition")).contains("student_course_report.xlsx");

        byte[] excelBytes = response.readEntity(byte[].class);
        assertThat(excelBytes).isNotEmpty();
        System.out.println("Excel size: " + excelBytes.length);
        Files.write(Path.of("C:\\Users\\plhristov\\test-report.xlsx"), excelBytes); // Write it out for inspection

        // Convert to Workbook using Apache POI
        try (ByteArrayInputStream bis = new ByteArrayInputStream(excelBytes);
             Workbook workbook = new XSSFWorkbook(bis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("Student Name");
            assertThat(headerRow.getCell(1).getStringCellValue()).isEqualTo("Total Credit");
        }
    }


    @Test
    @Order(6)
    void enrollStudents(){
        try {
            List<Student> students = MAPPER.readValue(getClass().getResource("/fixtures/students.json"), new TypeReference<List<Student>>() {});

            try (Response response = client
                    .target("http://localhost:%d/students/batch".formatted(localPort))
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(userJwtToken))
                    .post(Entity.json(students))) {
                assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
                List<URI> uris = response.readEntity(new GenericType<List<URI>>() {});
                assertEquals(2, uris.size());
                boolean stu00000 = uris.stream().allMatch(uri -> uri.toString().contains("STU00000"));
                assertTrue(stu00000);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }



    @Test
    @Order(7)
    void getEnrolledStudents(){
        Integer courseId = 1;
        int expected = 2;
        Response response = client
                .target("http://localhost:%d/students-courses/enrolled".formatted(localPort))
                .queryParam("courseId", courseId)
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(userJwtToken))
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Student> students = response.readEntity(new GenericType<List<Student>>() {});
        assertEquals( expected, students.size());
    }



}
