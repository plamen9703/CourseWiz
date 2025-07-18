package org.example.application.repository.users;

import org.example.application.api.users.User;
import org.example.application.repository.JdbcRepository;

import java.util.Optional;

public interface UserRepository <T extends User> extends JdbcRepository<T> {

    Optional<T> findByUsername(T user);

    Optional<T> findByEmail(T user);

    boolean existsByUsername(T user);

    boolean existsByEmail(T user);

}
