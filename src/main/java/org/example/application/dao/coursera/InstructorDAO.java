package org.example.application.dao.coursera;

import org.example.application.api.coursera.Instructor;
import org.example.application.repository.coursera.InstructorRepository;
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
        String query = "SELECT id, first_name, last_name, time_created FROM coursera.instructors;";
        return jdbcHelper.query(query, INSTRUCTOR_RESULT_SET_MAPPER);
    }

    @Override
    public Optional<Instructor> findById(Instructor instructor) {
        String sql="SELECT id, first_name, last_name, time_created FROM coursera.instructors WHERE id=?;";
        return jdbcHelper.querySingle(sql, INSTRUCTOR_RESULT_SET_MAPPER,instructor.getId());
    }

    @Override
    public Instructor insert(Instructor instructor) {
        String sql="INSERT INTO coursera.instructors( first_name, last_name, time_created) VALUES (?, ?);";
        return jdbcHelper.insert(sql,INSTRUCTOR_RESULT_SET_MAPPER, instructor.getFirstName(),instructor.getLastName());
    }

    @Override
    public int update(Instructor instructor) {
        String sql="UPDATE coursera.instructors SET  first_name=?, last_name=? WHERE id=?;";
        return jdbcHelper.update(sql, instructor.getFirstName(), instructor.getLastName(), instructor.getId());
    }

    @Override
    public int delete(Instructor instructor) {
        String sql="DELETE FROM coursera.instructors WHERE id=?;";
        return jdbcHelper.update(sql,instructor.getId());
    }

    @Override
    public boolean existsById(Instructor instructor) {
        String sql= "SELECT id, first_name, last_name, time_created FROM coursera.instructors WHERE id=?;";
        return jdbcHelper.exists(sql,instructor.getId());
    }
}
