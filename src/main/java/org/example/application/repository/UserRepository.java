package org.example.application.repository;

import org.example.application.api.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    User create(User user);

    void update(User user);

    void delete(User user);
}
