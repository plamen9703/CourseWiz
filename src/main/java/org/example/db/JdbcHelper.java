package org.example.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface JdbcHelper {

    Connection getConnection() throws SQLException;
    <T> List<T> query(String sql, ResultSetMapper<T> mapper, Object... params);
    <T> Optional<T> querySingle(String sql, ResultSetMapper<T> mapper, Object... params);
    int update(String sql, Object... params);
    <T> T insert(String sql, ResultSetMapper<T> keyMapper, Object... params);
    boolean exists(String sql, Object... params);
}
