package org.example.application.services.interfaces.users;

import org.example.application.api.users.User;
import org.example.application.services.interfaces.JdbcService;

public interface UserService<T extends User> extends JdbcService<T> {

    T findByUsername(T user);
    T findByEmail(T user);
    T findByUsernameOrEmail(T user);


    String login(T user);


}
