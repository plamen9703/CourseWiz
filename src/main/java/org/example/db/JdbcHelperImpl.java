package org.example.db;

import org.example.application.exceptions.EntityUpdateFailedException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcHelperImpl implements JdbcHelper{
    private final DataSource dataSource;

    public JdbcHelperImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public <T> List<T> query(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            setParams(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Query failed", e);
        }
        return results;
    }

    public <T> Optional<T> querySingle(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = query(sql, mapper, params);
        return results.stream().findFirst();
    }

    public int update(String sql, Object... params) {

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            conn.setAutoCommit(false);
            setParams(stmt, params);
            int i = stmt.executeUpdate();
            if(i==0){
                conn.rollback();
                throw new EntityUpdateFailedException("Database failed to update!");
            }
            conn.commit();
            return i;
        } catch (SQLException e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    public <T> T insert(String sql, ResultSetMapper<T> keyMapper, Object... params) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            conn.setAutoCommit(false);
            setParams(stmt, params);
//            int i=stmt.executeUpdate();
//            if(i==0){
//                conn.rollback();
//                throw new EntityInsertFailedException("Failed to insert entity!");
//            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T mapped = keyMapper.map(rs);
                    conn.commit();
                    return mapped;
                } else {
                    conn.rollback();
                    throw new RuntimeException("No generated key returned");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Insert failed", e);
        }
    }

    private void setParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }


    public boolean exists(String sql, Object... params) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            setParams(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row matches
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exists check failed", e);
        }
    }

}
