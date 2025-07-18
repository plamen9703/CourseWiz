package org.example.application.services.implementations.users;

import org.example.application.api.users.User;
import org.example.application.api.users.UserStudent;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.exceptions.UserLoginException;
import org.example.application.repository.users.UserStudentRepository;
import org.example.application.services.interfaces.users.UserStudentService;
import org.example.application.services.jwt.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.function.Function;

public class UserStudentServiceImpl implements UserStudentService {


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


    private final UserStudentRepository userStudentRepository;


    public UserStudentServiceImpl(UserStudentRepository userStudentRepositorypository) {
        this.userStudentRepository = userStudentRepositorypository;
    }



    private boolean isValidEmail(String input) {
        return input!=null && input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }


    @Override
    public UserStudent findByUsernameOrEmail(UserStudent userStudent) {
        if (userStudent.getUsername()!=null)
            return findByUsername(userStudent);
        if (userStudent.getEmail() != null)
            return findByEmail(userStudent);
        throw new NotFoundException("Missing user identifier username or email.");
    }

    @Override
    public UserStudent findByUsername(UserStudent user) {
        return userStudentRepository.findByUsername(user)
                .orElseThrow(()-> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getUsername()));
    }

    @Override
    public UserStudent findByEmail(UserStudent user) {
        return userStudentRepository.findByEmail(user)
                .orElseThrow(()->USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getEmail()));
    }

    @Override
    public String login(UserStudent userStudent) {
        User existing = findByUsernameOrEmail(userStudent);

        if (!BCrypt.checkpw(userStudent.getPassword(), existing.getPassword())){
            throw new UserLoginException("User password doesn't match!");
        }
        return JwtUtil.generateToken(existing);
    }


    @Override
    public List<UserStudent> findAll() {
        return userStudentRepository.findAll();
    }

    @Override
    public UserStudent findById(UserStudent userStudent) {
        return userStudentRepository.findById(userStudent)
                .orElseThrow(()-> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(userStudent.getUsername()));
    }

    @Override
    public UserStudent create(UserStudent userStudent) {
        if(userStudentRepository.existsByUsername(userStudent))
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(userStudent.getUsername());
        if(userStudentRepository.existsByEmail(userStudent))
            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(userStudent.getEmail());
        userStudent.setPassword(BCrypt.hashpw(userStudent.getPassword(), BCrypt.gensalt()));
        return userStudentRepository.insert(userStudent);
    }

    @Override
    public void update(UserStudent userStudent) {
        if (!userStudentRepository.existsById(userStudent))
            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(userStudent);
        userStudentRepository.update(userStudent);
    }

    @Override
    public void delete(UserStudent userStudent) {
        if (!userStudentRepository.existsById(userStudent))
            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(userStudent);
        userStudentRepository.delete(userStudent);
    }

    @Override
    public boolean existsById(UserStudent userStudent) {
        return userStudentRepository.existsById(userStudent);
    }
}
