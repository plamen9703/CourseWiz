package org.example.application.services.implementations.users;

import org.example.application.api.users.UserStudent;
import org.example.application.repository.users.UserStudentRepository;
import org.example.application.services.interfaces.users.UserStudentService;

public class UserStudentServiceImpl extends UserServiceIml<UserStudent> implements UserStudentService {


//    private static final Function<User, NotFoundException> USER_BY_ID_NOT_FOUND_EXCEPTION =
//            user -> new NotFoundException(
//                    String.format(
//                            "User ID: %d not found!",
//                            user.getId()
//                    )
//            );
//
//    private static final Function<String, NotFoundException> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION =
//            identifier -> new NotFoundException(
//                    String.format(
//                            "User with identifier: %s not found.",
//                            identifier
//                    )
//            );
//    private static final Function<String, DuplicateEntityException> USER_DUPLICATE_ENTITY_EXCEPTION =
//            identifier -> new DuplicateEntityException(
//                    String.format(
//                            "User with identifier: %s already exists.",
//                            identifier
//                    )
//            );
//
//    private static final Function<String, RuntimeException> USER_INVALID_EMAIL_EXCEPTION =
//            email -> new RuntimeException(String.format("User email is not valid. Email: %s", email));
//
//    private static final Supplier<RuntimeException> USER_PASSWORD_EXCEPTION =
//           () -> new RuntimeException("User password can not be empty.");
//    private static final Function<String,RuntimeException> USER_INVALID_USERNAME_EXCEPTION =
//             username -> new RuntimeException(String.format("Invalid user username! Username: %s", username));
//
//
//    private final UserStudentRepository userStudentRepository;


    public UserStudentServiceImpl(UserStudentRepository userStudentRepository) {
//        this.userStudentRepository = userStudentRepository;
        super(userStudentRepository);
    }


//
//    private boolean isValidEmail(String input) {
//        return input.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
//               /*||input.matches("^(.+)@(\\S+)$")*/;
//    }
//
//
//    @Override
//    public UserStudent findByUsernameOrEmail(UserStudent userStudent) {
//        if (userStudent.getUsername()!=null)
//            return findByUsername(userStudent);
//        if (userStudent.getEmail() != null)
//            return findByEmail(userStudent);
//        throw new NotFoundException("Missing user identifier username or email.");
//    }
//
//    @Override
//    public UserStudent findByUsername(UserStudent user) {
//        return userStudentRepository.findByUsername(user)
//                .orElseThrow(()-> USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getUsername()));
//    }
//
//    @Override
//    public UserStudent findByEmail(UserStudent user) {
//        if (!isValidEmail(user.getEmail()))
//            throw USER_INVALID_EMAIL_EXCEPTION.apply(user.getEmail());
//        return userStudentRepository.findByEmail(user)
//                .orElseThrow(()->USER_BY_IDENTIFIER_NOT_FOUND_EXCEPTION.apply(user.getEmail()));
//    }
//
//    @Override
//    public String login(UserStudent userStudent) {
//        if(userStudent.getPassword() == null || userStudent.getPassword().isEmpty())
//            throw USER_PASSWORD_EXCEPTION.get();
//        User existing = findByUsernameOrEmail(userStudent);
//        if (!BCrypt.checkpw(userStudent.getPassword(), existing.getPassword())){
//            throw new UserLoginException("User password doesn't match!");
//        }
//        return JwtUtil.generateToken(existing);
//    }
//
//
//    @Override
//    public List<UserStudent> findAll() {
//        return userStudentRepository.findAll();
//    }
//
//    @Override
//    public UserStudent findById(UserStudent userStudent) {
//        return userStudentRepository.findById(userStudent)
//                .orElseThrow(()-> USER_BY_ID_NOT_FOUND_EXCEPTION.apply(userStudent));
//    }
//
//    @Override
//    public UserStudent create(UserStudent userStudent) {
//        if(userStudent.getUsername()==null)
//            throw USER_INVALID_USERNAME_EXCEPTION.apply(userStudent.getUsername());
//        if(userStudentRepository.existsByUsername(userStudent))
//            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(userStudent.getUsername());
//        if (userStudent.getEmail()==null || !isValidEmail(userStudent.getEmail())) {
//            throw USER_INVALID_EMAIL_EXCEPTION.apply(userStudent.getEmail());
//        }
//        if(userStudentRepository.existsByEmail(userStudent))
//            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(userStudent.getEmail());
//        if(userStudent.getPassword() == null || userStudent.getPassword().isEmpty())
//            throw USER_PASSWORD_EXCEPTION.get();
//        userStudent.setPassword(BCrypt.hashpw(userStudent.getPassword(), BCrypt.gensalt()));
//        return userStudentRepository.insert(userStudent);
//    }
//
//    @Override
//    public void update(UserStudent userStudent) {
//        if(userStudent.getUsername()==null && userStudent.getPassword()==null && userStudent.getEmail()==null)
//            return;
//
//        if (!userStudentRepository.existsById(userStudent))
//            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(userStudent);
//
//        handleEserEmailChecks(userStudent);
//
//        if(userStudent.getUsername()!=null && userStudentRepository.existsByUsername(userStudent)){
//            throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(userStudent.getUsername());
//        }
//
//        if(userStudent.getPassword() !=null && !userStudent.getPassword().isEmpty()) {
//            userStudent.setPassword(BCrypt.hashpw(userStudent.getPassword(), BCrypt.gensalt()));
//        }
//
//        userStudentRepository.update(userStudent);
//    }
//
//    private void handleEserEmailChecks(UserStudent userStudent) {
//        if(userStudent.getEmail() != null){
//            if(!isValidEmail(userStudent.getEmail()))
//                throw USER_INVALID_EMAIL_EXCEPTION.apply(userStudent.getEmail());
//            if(userStudentRepository.existsByEmail(userStudent))
//                throw USER_DUPLICATE_ENTITY_EXCEPTION.apply(userStudent.getEmail());
//        }
//    }
//
//    @Override
//    public void delete(UserStudent userStudent) {
//        if (!userStudentRepository.existsById(userStudent))
//            throw USER_BY_ID_NOT_FOUND_EXCEPTION.apply(userStudent);
//        userStudentRepository.delete(userStudent);
//    }
//
//    @Override
//    public boolean existsById(UserStudent userStudent) {
//        return userStudentRepository.existsById(userStudent);
//    }
}
