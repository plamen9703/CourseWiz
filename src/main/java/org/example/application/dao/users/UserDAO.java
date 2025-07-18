package org.example.application.dao.users;

import org.example.application.api.users.User;
import org.example.application.repository.users.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserDAO implements UserRepository<User> {

    @Override
    public Optional<User> findByUsername(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(User user) {
        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(User user) {
        return false;
    }

    @Override
    public boolean existsByEmail(User user) {
        return false;
    }


    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(User entity) {
        return Optional.empty();
    }

    @Override
    public User insert(User entity) {
        return null;
    }

    @Override
    public int update(User entity) {
        return 0;
    }

    @Override
    public int delete(User entity) {
        return 0;
    }

    @Override
    public boolean existsById(User entity) {
        return false;
    }
}

