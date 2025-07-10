package org.example.application.dao;

import org.example.application.api.User;
import org.example.application.repository.UserRepository;
import org.example.db.JdbcHelper;
import org.example.db.ResultSetMapper;

import java.util.Optional;

public class UserDAO implements UserRepository {

    private final JdbcHelper jdbcHelper;

    private static final ResultSetMapper<User> USER_RESULT_SET_MAPPER= rs -> new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getTimestamp("created_at")
    );

    public UserDAO(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        String sql="SELECT id, username, email, password, role, created_at FROM public.users WHERE username=?;";
        return jdbcHelper.querySingle(sql,
                USER_RESULT_SET_MAPPER,
                userName);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql="SELECT id, username, email, password, role, created_at FROM public.users WHERE email=?;";
        return jdbcHelper.querySingle(sql,
                USER_RESULT_SET_MAPPER,
                email);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return findByUsername(userName).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public User create(User user) {
        String sql="INSERT INTO public.users (username, email, password, role) VALUES (?, ?, ?,?);";
        return jdbcHelper.insert(sql,USER_RESULT_SET_MAPPER,user.getUsername(),user.getEmail(),user.getPassword(), user.getRole());
    }

    @Override
    public void update(User user) {
        String sql="UPDATE public.users SET password=? WHERE id=?;";
        jdbcHelper.update(sql, user.getPassword(), user.getId());
    }

    @Override
    public void delete(User user) {
        String sql="DELETE FROM public.users WHERE id=?";
        jdbcHelper.update(sql,user.getId());
    }
}
