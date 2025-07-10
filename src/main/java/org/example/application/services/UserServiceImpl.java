package org.example.application.services;

import org.example.application.api.User;
import org.example.application.exceptions.InvalidUserCredentialsException;
import org.example.application.exceptions.UserLoginException;
import org.example.application.jwt.JwtService;
import org.example.application.repository.UserRepository;
import org.example.application.services.interfaces.UserService;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public Optional<User> findByUsernameorEmail(String identifier) {
        if (isValidEmail(identifier)) {
            return userRepository.findByEmail(identifier);
        } else {
            return userRepository.findByUsername(identifier);
        }
    }

    @Override
    public User create(User user) {
        if(!isValidEmail(user.getEmail())) {
            throw new InvalidUserCredentialsException("User email is not valid! Email: %s"
                    .formatted(user.getEmail()));
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        return userRepository.create(user);
    }

    @Override
    public void update(String identifier, User user) {
        User existing = findByUsernameorEmail(identifier)
                .orElseThrow(
                        ()->new NotFoundException("User with identifier %s NOT found"
                                .formatted(identifier)));
        user.setId(existing.getId());
        userRepository.update(user);
    }

    @Override
    public void delete(String identifier) {
        User existing = findByUsernameorEmail(identifier)
                .orElseThrow(
                        ()->new NotFoundException("User with identifier %s NOT found"
                                .formatted(identifier)));
        userRepository.delete(existing);
    }

    @Override
    public String login(User user) {
        User exiting=findByUsernameorEmail(user.getIdentifier()).orElseThrow(
                ()-> new NotFoundException("User with identifier %s NOT found"
                        .formatted(user.getIdentifier())));
        if (!BCrypt.checkpw(user.getPassword(), exiting.getPassword())){
            throw new UserLoginException("User login failed!");
        }
        return jwtService.generateToken(user);
    }

    private boolean isValidEmail(String input) {
        return input!=null && input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
}
