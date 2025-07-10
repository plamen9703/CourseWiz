package org.example.application.services.interfaces;

import org.example.application.api.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsernameorEmail(String identifier);

    User create( User user);

    void update(String identifier, User user);

    void delete(String identifier);

    String login(User user);
}
