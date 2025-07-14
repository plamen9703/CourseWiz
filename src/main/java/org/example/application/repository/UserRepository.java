package org.example.application.repository;

import org.example.application.api.User;

import java.util.Optional;

public interface UserRepository extends JdbcRepository<User>{

    Optional<User> findByUsername(User user);

    Optional<User> findByEmail(User user);

    boolean existsByUsername(User user);

    boolean existsByEmail(User user);

}
