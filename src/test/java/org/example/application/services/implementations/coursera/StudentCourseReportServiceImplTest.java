package org.example.application.services.implementations.coursera;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.application.api.coursera.Student;
import org.example.application.api.coursera.StudentCourseReport;
import org.example.application.dao.coursera.StudentCourseReportDAO;
import org.example.application.dao.coursera.StudentDAO;
import org.example.application.exceptions.StudentCourseReportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentCourseReportServiceImplTest {

    private final StudentCourseReportDAO studentCourseReportDAO = mock(StudentCourseReportDAO.class);
    private final StudentDAO studentDAO = mock(StudentDAO.class);

    private StudentCourseReportServiceImpl studentCourseReportService;

    @BeforeEach
    void setUp() {
        studentCourseReportService = new StudentCourseReportServiceImpl(studentCourseReportDAO, studentDAO);
    }

    @AfterEach
    void tearDown() {
        reset(studentCourseReportDAO, studentDAO);
    }

    @Test
    void testGetStudentCourseReports_HappyPath() throws StudentCourseReportException {
        List<String> pins = Arrays.asList("PIN123", "PIN456");
        List<StudentCourseReport> mockReport = Collections.singletonList(new StudentCourseReport());

        doReturn(true).when(studentDAO).existsById(any(Student.class)); // All pins valid
        when(studentCourseReportDAO.getStudentCourseReport(anyList(), any(), any(), any()))
                .thenReturn(mockReport);

        List<StudentCourseReport> result = studentCourseReportService.getStudentCourseReports(pins, 10, "2025-01-01", "2025-02-01");

        assertEquals(mockReport, result);
        verify(studentCourseReportDAO).getStudentCourseReport(eq(pins), eq(10), eq("2025-01-01"), eq("2025-02-01"));
    }

    @Test
    void testGetStudentCourseReports_InvalidStudentPins_ThrowsException() {
        List<String> pins = Arrays.asList("PIN123", "INVALID");
        Student studen1 = new Student();
        studen1.setPin("PIN123");
        doReturn(true).when(studentDAO).existsById(studen1);
        Student student2 = new Student();
        student2.setPin("INVALID");
        doReturn(false).when(studentDAO).existsById(student2);

        StudentCourseReportException ex = assertThrows(StudentCourseReportException.class, () ->
                studentCourseReportService.getStudentCourseReports(pins, 5, "2025-01-01", "2025-02-01"));

        assertTrue(ex.getMessage().contains("Invalid student pins"));
        verify(studentCourseReportDAO, never()).getStudentCourseReport(any(), any(), any(), any());
    }

    @Test
    void testGetStudentCourseReports_NullStudentPins_SkipsValidation() throws StudentCourseReportException {
        List<StudentCourseReport> mockReport = Collections.singletonList(new StudentCourseReport());
        when(studentCourseReportDAO.getStudentCourseReport(any(), any(), any(), any()))
                .thenReturn(mockReport);

        List<StudentCourseReport> result = studentCourseReportService.getStudentCourseReports(null, 10, null, null);

        assertEquals(mockReport, result);
    }

    @Test
    void testGetStudentCourseReports_MinimalCreditNegative_ThrowsException() {
        assertThrows(StudentCourseReportException.class, () ->
                studentCourseReportService.getStudentCourseReports(Collections.emptyList(), -1, "2025-01-01", "2025-02-01"));

        studentCourseReportService.getStudentCourseReports(Collections.emptyList(), null, "2025-01-01", "2025-02-01");
        verify(studentCourseReportDAO, times(1)).getStudentCourseReport(anyList(), eq(null), anyString(), anyString());

    }

    @Test
    void testGetStudentCourseReports_StartDateAfterEndDate_ThrowsException() {
        assertThrows(StudentCourseReportException.class, () ->
                studentCourseReportService.getStudentCourseReports(Collections.emptyList(), 5, "2025-02-01", "2025-01-01"));

        studentCourseReportService.getStudentCourseReports(Collections.emptyList(), 5, null, null);
        verify(studentCourseReportDAO, times(1)).getStudentCourseReport(anyList(), anyInt(), eq(null), eq(null));

    }

    @Test
    void testGetStudentCourseReports_NullDates_SkipsDateValidation() throws StudentCourseReportException {
        when(studentDAO.existsById(any(Student.class))).thenReturn(true);
        when(studentCourseReportDAO.getStudentCourseReport(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<StudentCourseReport> result1 = studentCourseReportService.getStudentCourseReports(Collections.singletonList("PIN123"), 5, null, null);
        List<StudentCourseReport> result2 = studentCourseReportService.getStudentCourseReports(Collections.singletonList("PIN123"), 5, null, "2025-05-21");
        List<StudentCourseReport> result3 = studentCourseReportService.getStudentCourseReports(Collections.singletonList("PIN123"), 5, "2025-05-21", null);
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        verify(studentCourseReportDAO).getStudentCourseReport(anyList(), eq(5), isNull(), isNull());
        verify(studentCourseReportDAO).getStudentCourseReport(anyList(), eq(5), anyString(), isNull());
        verify(studentCourseReportDAO).getStudentCourseReport(anyList(), eq(5), isNull(), anyString());
    }

    @Test
    void testStudentExists_whenExistsByIdReturnsTrue() {
        // Arrange
        when(studentDAO.existsById(any(Student.class))).thenReturn(true);

        // Act
        boolean result = studentCourseReportService.studentExists("PIN123");

        // Assert
        assertTrue(result);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).existsById(captor.capture());
        assertEquals("PIN123", captor.getValue().getPin());
    }

    @Test
    void testStudentExists_whenExistsByIdReturnsFalse() {
        // Arrange
        when(studentDAO.existsById(any(Student.class))).thenReturn(false);

        // Act
        boolean result = studentCourseReportService.studentExists("PIN456");

        // Assert
        assertFalse(result);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).existsById(captor.capture());
        assertEquals("PIN456", captor.getValue().getPin());
    }


    @Test
    void testGenerateExcelReport_withEmptyList() throws Exception {
        ByteArrayOutputStream outputStream = studentCourseReportService.generateExcelReport(List.of());
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0); // Workbook exists

        Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
        Sheet sheet = wb.getSheet("Student Report");
        assertNotNull(sheet);
        assertEquals(-1, sheet.getLastRowNum()); // No rows because no data
        wb.close();
    }

    @Test
    void testGenerateExcelReport_singleStudentSingleCourse() throws Exception {
        StudentCourseReport record = new StudentCourseReport();
        record.setStudentPin("123");
        record.setStudentName("John Doe");
        record.setTotalCredit(10);
        record.setCourseName("Math");
        record.setTotalTime((short) 5);
        record.setCredit((short) 3);
        record.setInstructorName("Prof. Smith");
        ByteArrayOutputStream outputStream = studentCourseReportService.generateExcelReport(List.of(record));

        Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
        Sheet sheet = wb.getSheet("Student Report");

        // Validate Student Header Row
        Row headerRow = sheet.getRow(1);
        assertEquals("Student Name", headerRow.getCell(0).getStringCellValue());
        assertEquals("Total Credit", headerRow.getCell(1).getStringCellValue());

        // Validate Student Data Row
        Row studentRow = sheet.getRow(2);
        assertEquals("John Doe", studentRow.getCell(0).getStringCellValue());
        assertEquals(10, studentRow.getCell(1).getNumericCellValue());

        // Validate Course Header Row
        Row courseHeader = sheet.getRow(3);
        assertEquals("Completed", courseHeader.getCell(0).getStringCellValue());

        // Validate Course Data Row
        Row courseRow = sheet.getRow(4);
        assertEquals("-->", courseRow.getCell(0).getStringCellValue());
        assertEquals("Math", courseRow.getCell(1).getStringCellValue());
        assertEquals(5, courseRow.getCell(2).getNumericCellValue());
        assertEquals(3, courseRow.getCell(3).getNumericCellValue());
        assertEquals("Prof. Smith", courseRow.getCell(4).getStringCellValue());

        wb.close();
    }

    @Test
    void testGenerateExcelReport_singleStudentMultipleCourses() throws Exception {
        StudentCourseReport record1 = new StudentCourseReport();
        record1.setStudentPin("123");
        record1.setStudentName("John Doe");
        record1.setTotalCredit(10);
        record1.setCourseName("Math");
        record1.setTotalTime((short) 5);
        record1.setCredit((short) 3);
        record1.setInstructorName("Prof. Smith");
        StudentCourseReport record2 = new StudentCourseReport();
        record2.setStudentPin("123");
        record2.setStudentName("John Doe");
        record2.setTotalCredit(10);
        record2.setCourseName("Physics");
        record2.setTotalTime((short) 8);
        record2.setCredit((short) 4);
        record2.setInstructorName("Prof. Brown");

        ByteArrayOutputStream outputStream = studentCourseReportService.generateExcelReport(List.of(record1, record2));
        Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
        Sheet sheet = wb.getSheet("Student Report");

        // Student header should appear only once
        assertEquals("Student Name", sheet.getRow(1).getCell(0).getStringCellValue());
        // Validate that both courses are listed
        assertEquals("Math", sheet.getRow(4).getCell(1).getStringCellValue());
        assertEquals("Physics", sheet.getRow(5).getCell(1).getStringCellValue());

        wb.close();
    }

    @Test
    void testGenerateExcelReport_multipleStudents() throws Exception {
        StudentCourseReport student1Course1 = new StudentCourseReport();
        student1Course1.setStudentPin("123");
        student1Course1.setStudentName("John Doe");
        student1Course1.setTotalCredit(10);
        student1Course1.setCourseName("Math");
        student1Course1.setTotalTime((short) 5);
        student1Course1.setCredit((short) 3);
        student1Course1.setInstructorName("Prof. Smith");

        StudentCourseReport student2Course1 = new StudentCourseReport();
        student2Course1.setStudentPin("456");
        student2Course1.setStudentName("Jane Smith");
        student2Course1.setTotalCredit(8);
        student2Course1.setCourseName("Biology");
        student2Course1.setTotalTime((short) 6);
        student2Course1.setCredit((short) 2);
        student2Course1.setInstructorName("Prof. Adams");


        ByteArrayOutputStream outputStream = studentCourseReportService.generateExcelReport(List.of(student1Course1, student2Course1));
        Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
        Sheet sheet = wb.getSheet("Student Report");

        // Check both students exist
        assertTrue(sheet.getRow(2).getCell(0).getStringCellValue().equals("John Doe"));
        assertTrue(sheet.getRow(6).getCell(0).getStringCellValue().equals("Jane Smith"));

        wb.close();
    }

}