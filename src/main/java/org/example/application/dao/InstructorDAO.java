package org.example.application.dao;

import org.example.application.api.Instructor;
import org.example.application.repository.InstructorRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.List;
import java.util.Optional;

public class InstructorDAO implements InstructorRepository {

    private final JdbcHelper jdbcHelper;

    private static final ResultSetMapper<Instructor> INSTRUCTOR_RESULT_SET_MAPPER= rs->new Instructor(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getTimestamp("time_created"));

    public InstructorDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper=jdbcHelper;
    }

    @Override
    public List<Instructor> findAll() {
        return jdbcHelper.query("SELECT id, first_name, last_name, time_created FROM public.instructors;", INSTRUCTOR_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<Instructor> findById(int instructorId) {
        String sql="SELECT id, first_name, last_name, time_created FROM public.instructors WHERE id=?;";
        return jdbcHelper.querySingle(sql, INSTRUCTOR_RESULT_SET_MAPPER,instructorId);
    }

    @Override
    public Instructor create(Instructor instructor) {
        String sql="INSERT INTO public.instructors( first_name, last_name, time_created) VALUES (?, ?, COALESCE(?, CURRENT_TIMESTAMP));";
        return jdbcHelper.insert(sql,INSTRUCTOR_RESULT_SET_MAPPER, instructor.getFirstName(),instructor.getLastName(), instructor.getTimeCreated());
    }

    @Override
    public int update(Integer instructorId,Instructor instructor) {
        String sql="UPDATE public.instructors SET  first_name=?, last_name=? WHERE id=?;";
        return jdbcHelper.update(sql, instructor.getFirstName(), instructor.getLastName(), instructorId);
    }

    @Override
    public int delete(Integer instructorId) {
        String sql="DELETE FROM public.instructors WHERE id=?;";
        return jdbcHelper.update(sql,instructorId);
    }

    @Override
    public boolean existsById(int instructorId) {
        String sql= "SELECT id, first_name, last_name, time_created FROM public.instructors WHERE id=?;";
        return jdbcHelper.exists(sql,instructorId);
    }
}
