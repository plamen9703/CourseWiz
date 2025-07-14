package org.example.application.services.interfaces;

import org.example.application.api.User;

public interface UserService extends JdbcService<User>{


    User findByUsername(User user);
    User findByEmail(User user);

    String login(User user);
}
