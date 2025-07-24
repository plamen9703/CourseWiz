package org.example.application.services.implementations.users;

import org.example.application.api.users.User;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.exceptions.UserLoginException;
import org.example.application.repository.users.UserRepository;
import org.example.application.services.interfaces.users.UserService;
import org.example.application.services.jwt.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class UserServiceIml<T extends User> implements UserService<T> {
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

    private static final Function<String, RuntimeException> USER_INVALID_EMAIL_EXCEPTION =
            email -> new RuntimeException(String.format("User email is not valid. Email: %s", email));

    private static final Supplier<RuntimeException> USER_PASSWORD_EXCEPTION =
            () -> new RuntimeException("User password can not be empty.");
    private static final Function<String,RuntimeException> USER_INVALID_USERNAME_EXCEPTION =
            username -> new RuntimeException(String.format("Invalid user username! Username: %s", username));
    private static final Logger log = LoggerFactory.getLogger(UserServiceIml.class);


    protected final UserRepository<T> userRepository;


    public UserServiceIml(UserRepository<T> userRepository) {
        this.userRepository = userRepository;
    }



    private boolean isValidEmail(String input) {
        return input.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                /*||input.matches("^(.+)@(\\S+)$")*/;
    }


    @Override
    public T findByUsernameOrEmail(T user) throws RuntimeException {
        if (user.getUsername()!=null)
            return findByUsername(user);
        if (user.getEmail() != null)
            return findByEmail(user);
        throw new NotFoundException("Missing user identifier username or email.");
    }

    @Override
    public T findByUsername(T user) throws RuntimeException {
        return userRepository.findByUsername(user)
                .orElseThrow(()-> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getUsername()));
    }

    @Override
    public T findByEmail(T user) throws RuntimeException{
        if (!isValidEmail(user.getEmail()))
            throw USER_INVALID_EMAIL_EXCEPTION.apply(user.getEmail());
        return userRepository.findByEmail(user)
                .orElseThrow(()->USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getEmail()));
    }

    @Override
    public String login(T user) throws RuntimeException{
        if(user.getPassword() == null || user.getPassword().isEmpty())
            throw USER_PASSWORD_EXCEPTION.get();
        T existing = findByUsernameOrEmail(user);
        if (!BCrypt.checkpw(user.getPassword(), existing.getPassword())){
            throw new UserLoginException("User password doesn't match!");
        }
        return JwtUtil.generateToken(existing);
    }


    @Override
    public List<T> findAll() {
        return userRepository.findAll();
    }

    @Override
    public T findById(T user) {
        return userRepository.findById(user)
                .orElseThrow(()-> USER_BY_ID_NOT_FOUND_EXCEPTION.apply(user));
    }

    @Override
    public T create(T user) {
        if(user.getUsername()==null)
            throw USER_INVALID_USERNAME_EXCEPTION.apply(user.getUsername());
        if(userRepository.existsByUsername(user))
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(user.getUsername());
        if (user.getEmail()==null || !isValidEmail(user.getEmail())) {
            throw USER_INVALID_EMAIL_EXCEPTION.apply(user.getEmail());
        }
        if(userRepository.existsByEmail(user))
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(user.getEmail());
        if(user.getPassword() == null || user.getPassword().isEmpty())
            throw USER_PASSWORD_EXCEPTION.get();
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userRepository.insert(user);
    }

    @Override
    public void update(T user) {
        String password = user.getPassword();
        if(user.getUsername()==null && password ==null && user.getEmail()==null)
            return;

        if (!userRepository.existsById(user))
            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(user);

        handleEserEmailChecks(user);

        if(user.getUsername()!=null && userRepository.existsByUsername(user)){
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(user.getUsername());
        }

        if(password !=null && !password.isEmpty()) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        userRepository.update(user);
    }

    private void handleEserEmailChecks(T user) {
        if(user.getEmail() != null){
            if(!isValidEmail(user.getEmail()))
                throw USER_INVALID_EMAIL_EXCEPTION.apply(user.getEmail());
            if(userRepository.existsByEmail(user))
                throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(user.getEmail());
        }
    }

    @Override
    public void delete(T user) {
        if (!userRepository.existsById(user))
            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(user);
        userRepository.delete(user);
    }

    @Override
    public boolean existsById(T user) {
        return userRepository.existsById(user);
    }
}
