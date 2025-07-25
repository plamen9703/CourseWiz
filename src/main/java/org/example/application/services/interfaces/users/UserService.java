package org.example.application.services.interfaces.users;

import org.example.application.api.users.User;
import org.example.application.services.interfaces.JdbcService;

public interface UserService<T extends User> extends JdbcService<T> {

    T findByUsername(T user)throws RuntimeException;
    T findByEmail(T user)throws RuntimeException;
    T findByUsernameOrEmail(T user)throws RuntimeException;


    String login(T user);


}
