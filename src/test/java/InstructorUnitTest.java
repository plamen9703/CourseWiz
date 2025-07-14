import org.example.application.dao.InstructorDAO;
import org.example.application.repository.InstructorRepository;
import org.example.application.services.implementations.InstructorServiceImpl;
import org.example.application.services.interfaces.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class InstructorUnitTest {

    private InstructorRepository instructorRepository;
    private InstructorService instructorService;


    @BeforeEach
    public void setup() {
        instructorRepository = mock(InstructorDAO.class);
        instructorService = new InstructorServiceImpl(instructorRepository);
    }

    @Test
    public void createInstructor(){
//        Instructor instructor =new Instructor();
//        instructor.setId(1);
//        instructor.setFirstName("plamen");
//        instructor.setLastName("hristov");
//        when(instructorRepository.findById(1)).thenReturn(Optional.of(instructor));
//        Instructor result = instructorService.findById(1).get();
//        assertEquals(instructor,result);
//        assertNotNull(result);
//        assertEquals("plamen", result.getFirstName());
//        assertEquals("hristov", result.getLastName());
    }

    void updateInstrucot(){

    }
}
