package org.example.application.services.implementations;

import org.example.application.api.User;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.exceptions.UserLoginException;
import org.example.application.repository.UserRepository;
import org.example.application.services.auth.AuthServices;
import org.example.application.services.interfaces.UserService;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class UserServiceImpl implements UserService {


    private static final Function<User, NotFoundException> USER_BY_ID_NOT_FOUND_EXCEPTION =
            user -> new NotFoundException(
                    String.format(
                            "User ID: %d not found!",
                            user.getId()
                    )
            );

    private static final Function<String, NotFoundException> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION =
            identifier -> new NotFoundException(
                    String.format(
                            "User with identifier: %s not found.",
                            identifier
                    )
            );
    private static final Function<String, DuplicateEntityException> USER_DUPLICATE_ENTITY_EXCEPTION =
            identifier -> new DuplicateEntityException(
                    String.format(
                            "User with identifier: %s already exists.",
                            identifier
                    )
            );


    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    private boolean isValidEmail(String input) {
        return input!=null && input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }


    @Override
    public User findByUsername(User user) {
        return userRepository.findByUsername(user)
                .orElseThrow(()-> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getUsername()));
    }

    @Override
    public User findByEmail(User user) {
        return userRepository.findByEmail(user)
                .orElseThrow(()->USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getEmail()));
    }

    @Override
    public String login(User user) {
        User exiting=findByUsername(user);
        if (!BCrypt.checkpw(user.getPassword(), exiting.getPassword())){
            throw new UserLoginException("User login failed!");
        }
        return AuthServices.JWT_SERVICE.generateToken(exiting);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(User user) {
        return userRepository.findById(user)
                .orElseThrow(()-> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getUsername()));
    }

    @Override
    public User create(User user) {
        if(userRepository.existsByUsername(user))
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(user.getUsername());
        if(userRepository.existsByEmail(user))
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(user.getEmail());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userRepository.insert(user);
    }

    @Override
    public void update(User user) {
        if (!userRepository.existsById(user))
            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(user);
        userRepository.update(user);
    }

    @Override
    public void delete(User user) {
        if (!userRepository.existsById(user))
            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(user);
        userRepository.delete(user);
    }

    @Override
    public boolean existsById(User user) {
        return userRepository.existsById(user);
    }
}
