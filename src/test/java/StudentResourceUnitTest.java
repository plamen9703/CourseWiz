import org.example.application.api.Student;
import org.example.application.resource.StudentResource;
import org.example.application.services.implementations.StudentServiceImpl;
import org.example.application.services.interfaces.StudentService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import  io.dropwizard.testing.junit5.ResourceExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentResourceUnitTest {

    private static final StudentService STUDENT_SERVICE = Mockito.mock(StudentServiceImpl.class);
    private ResourceExtension resources;

    @BeforeAll
    void setUpResource() throws Throwable {
        // Manually create ResourceExtension for Dropwizard
        resources = ResourceExtension.builder()
                .addResource(new StudentResource(STUDENT_SERVICE))
                .build();
        resources.before(); // Start the test container
    }

    @AfterAll
    void tearDownResource() throws Throwable {
        resources.after(); // Stop the test container
    }

    @BeforeEach
    void setupMocks() {
        // Mock behavior for service
        when(STUDENT_SERVICE.findById(any(Student.class)))
                .thenReturn(new Student("1", "Plamen", "Hristov", null));
    }

    @Test
    void testGetStudent() {
        Student response = resources.target("/students/1")
                .request()
                .get(Student.class);

        assertNotNull(response);
        assertEquals("Plamen", response.getFirstName());
        assertEquals("Hristov", response.getLastName());
    }
}
