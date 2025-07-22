package org.example.application.services.implementations.users;

import org.example.application.api.users.UserInstructor;
import org.example.application.repository.users.UserInstructorRepository;
import org.example.application.services.interfaces.users.UserInstructorService;

public class UserInstructorServiceImpl extends UserServiceIml<UserInstructor> implements UserInstructorService {

//    private static final Function<UserInstructor, NotFoundException> USER_INSTRUCTOR_NOT_FOUND_EXCEPTION =
//            userInstructor -> new NotFoundException(String.format("User with ID: %d not found.", userInstructor.getId()));
//    private static final Function<String, DuplicateEntityException> USER_INSTRUCTOR_IDENTIFIER_DUPLICATE_ENTITY_EXCEPTION =
//            userIdentifier -> new DuplicateEntityException(String.format("User with Identifier: %s already exists.", userIdentifier));

//    private final UserInstructorRepository userInstructorRepository;

    public UserInstructorServiceImpl(UserInstructorRepository userInstructorRepository) {
//        this.userInstructorRepository = userInstructorRepository;
        super(userInstructorRepository);
    }

//    private boolean isValidEmail(String input) {
//        return input.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
//                /*||input.matches("^(.+)@(\\S+)$")*/;
//    }
//
//    @Override
//    public List<UserInstructor> findAll() {
//        return userInstructorRepository.findAll();
//    }
//
//    @Override
//    public UserInstructor findById(UserInstructor userInstructor) {
//        return userInstructorRepository.findById(userInstructor)
//                .orElseThrow(() -> USER_INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(userInstructor));
//    }
//
//    @Override
//    public UserInstructor create(UserInstructor userInstructor) {
        // check for exists usernames and emails

//        if (userInstructor.get)


//        if(userRepository.existsByUsername(userInstructor))
//            throw USER_INSTRUCTOR_IDENTIFIER_DUPLICATE_ENTITY_EXCEPTION.apply(userInstructor.getUsername());
//        if(userRepository.existsByEmail(userInstructor))
//            throw USER_INSTRUCTOR_IDENTIFIER_DUPLICATE_ENTITY_EXCEPTION.apply(userInstructor.getEmail());
//
//        // hash the password
//        userInstructor.setPassword(BCrypt.hashpw(userInstructor.getPassword(), BCrypt.gensalt()));
//
//        return userRepository.insert(userInstructor);
//    }
//
//    @Override
//    public void update(UserInstructor userInstructor) {
//        // check for existing users instructor
//        if(userInstructorRepository.existsById(userInstructor))
//            throw USER_INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(userInstructor);
//        //if username and password are not null the user wants to change them
//        //check if a user with the username or email already exists
//        if(userInstructor.getEmail() != null && userInstructorRepository.existsByEmail(userInstructor))
//            throw USER_INSTRUCTOR_IDENTIFIER_DUPLICATE_ENTITY_EXCEPTION.apply(userInstructor.getEmail());
//        if(userInstructor.getUsername() != null && userInstructorRepository.existsByUsername(userInstructor))
//            throw USER_INSTRUCTOR_IDENTIFIER_DUPLICATE_ENTITY_EXCEPTION.apply(userInstructor.getUsername());
//        //if password is not null the user wants to change it
//        //hash the new password
//        userInstructor.setPassword(BCrypt.hashpw(userInstructor.getPassword(),BCrypt.gensalt()));
//
//        userInstructorRepository.update(userInstructor);
//    }
//
//    @Override
//    public void delete(UserInstructor userInstructor) {
//        if(userInstructorRepository.existsById(userInstructor))
//            throw USER_INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(userInstructor);
//        userInstructorRepository.delete(userInstructor);
//    }
//
//    @Override
//    public boolean existsById(UserInstructor userInstructor) {
//        return userInstructorRepository.existsById(userInstructor);
//    }
//
//    @Override
//    public String login(UserInstructor userInstructor) {
//        UserInstructor found = findByUsernameOrEmail(userInstructor);
//        if (!BCrypt.checkpw(found.getPassword(), userInstructor.getPassword())) {
//            throw new UserLoginException("User password doesn't match!");
//        }
//        return JwtUtil.generateToken(found);
//    }
//
//
//    @Override
//    public UserInstructor findByUsername(UserInstructor userInstructor) {
//        return userInstructorRepository.findByUsername(userInstructor).orElseThrow(()->USER_INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(userInstructor));
//    }
//
//    @Override
//    public UserInstructor findByEmail(UserInstructor userInstructor) {
//        return userInstructorRepository.findByEmail(userInstructor).orElseThrow(()->USER_INSTRUCTOR_NOT_FOUND_EXCEPTION.apply(userInstructor));
//    }
//
//    @Override
//    public UserInstructor findByUsernameOrEmail(UserInstructor userInstructor) {
//        if(userInstructor.getEmail()!=null && !userInstructor.getEmail().isEmpty())
//            return findByEmail(userInstructor);
//        if(userInstructor.getUsername()!=null && !userInstructor.getUsername().isEmpty())
//            return findByUsername(userInstructor);
//        throw new UserLoginException("Can not log in! Missing user username and email!");
//    }
}
