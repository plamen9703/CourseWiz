package org.example.application.dao;

import org.example.application.api.StudentCourseReport;
import org.example.application.repository.StudentCourseReportRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.List;

public class StudentCourseReportDAO implements StudentCourseReportRepository {

    private final JdbcHelper jdbcHelper;
    private static final ResultSetMapper<StudentCourseReport> STUDENT_COURSE_REPORT_RESULT_SET_MAPPER = rs -> new StudentCourseReport(
            rs.getString("Student PIN"),
            rs.getString("Student Name"),
            rs.getInt("Total Credit"),
            rs.getInt("Course ID"),
            rs.getString("Course Name"),
            rs.getShort("Total Time"),
            rs.getShort("Credit"),
            rs.getString("Instructor Name")
    );
    public StudentCourseReportDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }

    public List<StudentCourseReport> getStudentCourseReport(List<String> studentPins, Integer minimalCredit, String startDate, String endDate){
        String sql="SELECT * FROM get_student_course_report(?::varchar[], ?, ?, ?);";
        return jdbcHelper.query(sql, STUDENT_COURSE_REPORT_RESULT_SET_MAPPER,
                studentPins==null || studentPins.isEmpty() ? null:
                "{"+String.join(",", studentPins)+"}",
                minimalCredit,
                startDate,
                endDate);
    }
}
