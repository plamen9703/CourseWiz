package org.example.application.services.implementations.users;

import org.example.application.api.users.User;
import org.example.application.dao.users.UserDAO;
import org.example.application.dao.users.UserInstructorDAO;
import org.example.application.dao.users.UserStudentDAO;
import org.example.application.exceptions.DuplicateEntityException;
import org.example.application.services.interfaces.users.UserInstructorService;
import org.example.application.services.interfaces.users.UserStudentService;
import org.example.application.services.jwt.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserDAO userDAO = mock(UserDAO.class);

    private UserServiceIml userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceIml(userDAO);
        UserInstructorService userInstructorService = new UserInstructorServiceImpl(mock(UserInstructorDAO.class));
        UserStudentService userStudentService = new UserStudentServiceImpl(mock(UserStudentDAO.class));
    }

    @AfterEach
    void tearDown() {
        reset(userDAO);
    }

    @Test
    void findByUsernameOrEmailWhenExceptionIsThrown() {
        User user = new User();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findByUsernameOrEmail(user));
        assertEquals("Missing user identifier username or email.", exception.getMessage());
        verify(userDAO, never()).findByEmail(any(User.class));
        verify(userDAO, never()).findByUsername(any(User.class));
    }

    @Test
    void findByUsernameOrEmailWithEmail() {
        User user = new User();
        user.setEmail("plamen@example.com");

        User userReturned = new User();
        userReturned.setUsername("plamen");
        userReturned.setEmail("plamen@example.com");
        when(userDAO.findByEmail(user)).thenReturn(Optional.of(userReturned));
        User found = userService.findByUsernameOrEmail(user);
        assertEquals(userReturned, found);
        verify(userDAO, times(1)).findByEmail(any(User.class));
    }

    @Test
    void findByUsernameOrEmailWithUsername() {
        User user = new User();
        user.setUsername("plamen");

        User userReturned = new User();
        userReturned.setUsername("plamen");
        userReturned.setEmail("plamen@example.com");
        when(userDAO.findByUsername(user)).thenReturn(Optional.of(userReturned));
        User found = userService.findByUsernameOrEmail(user);
        assertEquals(userReturned, found);
        verify(userDAO, times(1)).findByUsername(any(User.class));
    }

    @Test
    void findByUsernameWithEmptyOptional() {
        when(userDAO.findByUsername(any(User.class))).thenReturn(Optional.empty());
        User user = new User();
        user.setUsername("plamen");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findByUsername(user));
        assertEquals("User with identifier: plamen not found.", exception.getMessage());
    }

    @Test
    void findByEmailWithEmptyOptional() {
        when(userDAO.findByEmail(any(User.class))).thenReturn(Optional.empty());
        User user = new User();
        user.setEmail("plamen@example.com");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findByEmail(user));
        assertEquals("User with identifier: plamen@example.com not found.", exception.getMessage());
    }

    @Test
    void findByEmailWithInvalidEmail() {
        User user=new User();
        user.setEmail("plamen@example");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findByEmail(user));
        assertEquals("User email is not valid. Email: plamen@example", exception.getMessage());
    }

    @Test
    void loginWhenPasswordIsNullOrEmpty() {
        User user = new User();
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> userService.login(user));
        user.setPassword("");
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> userService.login(user));
        assertEquals("User password can not be empty.",exception1.getMessage());
        assertEquals("User password can not be empty.",exception2.getMessage());

    }

    @Test
    void loginWhenUsernameAndEmailAreNull() {
        User user = new User();
        user.setPassword("plamen123");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(user));
        assertEquals("Missing user identifier username or email.",exception.getMessage());

    }

    @Test
    void loginWhenUserPasswordDoesNotMatch() {
        User user = new User();
        user.setUsername("plamen");
        user.setPassword("plamen12");
        User userReturned = new User();
        userReturned.setPassword(BCrypt.hashpw("plamen123", BCrypt.gensalt()));
        when(userDAO.findByUsername(any(User.class))).thenReturn(Optional.of(userReturned));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(user));
        assertEquals("User password doesn't match!",exception.getMessage());

    }

    @Test
    void loginWhenUserPasswordDoMatch() {
        User user = new User();
        user.setUsername("plamen");
        user.setPassword("plamen123");

        String tokenExpected = JwtUtil.generateToken(user);

        User userReturned = new User();
        userReturned.setPassword(BCrypt.hashpw("plamen123", BCrypt.gensalt()));

        when(userDAO.findByUsername(any(User.class))).thenReturn(Optional.of(userReturned));
        String token = userService.login(user);


        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.startsWith("ey"));
    }

    @Test
    void findAll() {
        when(userDAO.findAll()).thenReturn(List.of());
        assertEquals(0, userService.findAll().size());
    }

    @Test
    void findByIdWhenUserDoesNotExist() {
        User user = new User();
        user.setId(1);
        when(userDAO.findById(any(User.class))).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findById(user));
        assertEquals("User ID: 1 not found!", exception.getMessage());
    }

    @Test
    void findByIdWhenUserDoesExist() {
        User user = new User();
        user.setId(1);

        User userReturned = new User();
        userReturned.setId(1);

        when(userDAO.findById(any(User.class))).thenReturn(Optional.of(userReturned));
        User found = userService.findById(user);
        assertEquals(user, found);
    }

    @Test
    void createWhenEmailExists() {
        when(userDAO.existsByEmail(any(User.class))).thenReturn(true);
        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);
        User user = new User();
        user.setUsername("plamen");
        user.setEmail("plamen@example.com");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("User with identifier: plamen@example.com already exists.", exception.getMessage());
        verify(userDAO, never()).insert(any(User.class));
    }

    @Test
    void createWhenUsernameExists() {
        when(userDAO.existsByUsername(any(User.class))).thenReturn(true);
        User user = new User();
        user.setUsername("plamen");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("User with identifier: plamen already exists.", exception.getMessage());
        verify(userDAO, never()).insert(any(User.class));
    }

    @Test
    void createWhenUsernameIsNull() {
        User user = new User();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("Invalid user username! Username: null", exception.getMessage());
        verify(userDAO, never()).insert(any(User.class));
    }

    @Test
    void createWhenEmailIsNull() {
        User user = new User();
        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);
        user.setUsername("plamen");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("User email is not valid. Email: null", exception.getMessage());
        verify(userDAO, never()).insert(any(User.class));
    }
    @Test
    void createWhenEmailIsNotValid() {
        User user = new User();
        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);
        user.setUsername("plamen");
        user.setEmail("plamen@example");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("User email is not valid. Email: plamen@example", exception.getMessage());
        verify(userDAO, never()).insert(any(User.class));
    }

    @Test
    void createWhenPasswordIsEmpty() {
        User user = spy(new User());
        when(userDAO.existsByEmail(any(User.class))).thenReturn(false);
        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);
        user.setUsername("plamen");
        user.setEmail("plamen@gmail.com");
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> userService.create(user));
        user.setPassword("");
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("User password can not be empty.", exception1.getMessage());
        assertEquals("User password can not be empty.", exception2.getMessage());
        verify(user, times(1)).setPassword(anyString());
        verify(userDAO, never()).insert(any(User.class));
    }

    @Test
    void createWhenHappyCase() {
        User user = spy(new User());
        user.setEmail("plamen@gmail.com");
        user.setUsername("plamen");
        user.setPassword("plamen123");

        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);
        when(userDAO.existsByEmail(any(User.class))).thenReturn(false);
        User userReturned = new User();
        userReturned.setEmail("plamen@example.com");
        userReturned.setUsername("plamen");
        when(userDAO.insert(user)).thenReturn(userReturned);

        User created = userService.create(user);

        assertNotNull(created);
        assertEquals(userReturned.getEmail(), created.getEmail());
        assertEquals(userReturned.getUsername(), created.getUsername());
        assertNull(created.getPassword());


        verify(user, times(2)).setPassword(anyString());
        verify(userDAO).insert(any(User.class));
    }


    @Test
    void updateWhenUserDoesNotExist() {
        when(userDAO.existsById(any(User.class))).thenReturn(false);
        User user = new User();
        user.setId(1);
        user.setUsername("");
        user.setPassword("");
        user.setEmail("");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.update(user));
        assertEquals("User ID: 1 not found!", exception.getMessage());
        verify(userDAO, never()).update(any(User.class));
    }


    @Test
    void updateWhenUserExistsAndNewEmailIsNotValid() {
        when(userDAO.existsById(any(User.class))).thenReturn(true);
        User user = new User();
        user.setId(1);
        user.setEmail("plamen");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.update(user));
        assertEquals("User email is not valid. Email: plamen", exception.getMessage());
        verify(userDAO, times(0)).update(any(User.class));
    }

    @Test
    void updateWhenUserExistsAndNewEmailIsTaken() {
        when(userDAO.existsById(any(User.class))).thenReturn(true);
        when(userDAO.existsByEmail(any(User.class))).thenReturn(true);
        User user = new User();
        user.setId(1);
        user.setEmail("plamen@example.com");
        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> userService.update(user));
        assertEquals("User with identifier: plamen@example.com already exists.", exception.getMessage());
        verify(userDAO, times(0)).update(any(User.class));
    }

    @Test
    void updateWhenUserExistsAndNewUsernameIsTaken() {
        when(userDAO.existsById(any(User.class))).thenReturn(true);
        when(userDAO.existsByUsername(any(User.class))).thenReturn(true);
        User user = new User();
        user.setId(1);
        user.setUsername("plamen");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.update(user));
        assertEquals("User with identifier: plamen already exists.", exception.getMessage());
        verify(userDAO, times(0)).update(any(User.class));
    }

    @Test
    void updateWhenUserExistsAndNewUsernameAndEmailIsValidAndPasswordIsNullOrEmpty() {
        when(userDAO.existsById(any(User.class))).thenReturn(true);
        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);
        when(userDAO.existsByEmail(any(User.class))).thenReturn(false);
        User user = spy(new User());
        user.setId(1);
        user.setUsername("plamen");
        user.setEmail("plamen@gmail.com");
        userService.update(user);
        user.setPassword("");
        userService.update(user);
        verify(user, times(1)).setPassword(anyString());
        verify(userDAO, times(2)).update(any(User.class));
    }

    @Test
    void updateWhenUserNewUsernameEmailPasswordAreNull() {
        User user = new User();
        userService.update(user);
        verify(userDAO, never()).existsByEmail(any(User.class));
        verify(userDAO, never()).existsByUsername(any(User.class));
        verify(userDAO, never()).existsById(any(User.class));
        verify(userDAO, never()).update(any(User.class));

    }

    @Test
    void updateWhenPasswordIsNotNullOrEmpty(){
        User user = new User();
        user.setPassword("plamen123");
        user.setUsername("plamen");
        user.setEmail("plamen@example.com");
        user.setId(1);

        when(userDAO.existsById(any(User.class))).thenReturn(true);
        when(userDAO.existsByEmail(any(User.class))).thenReturn(false);
        when(userDAO.existsByUsername(any(User.class))).thenReturn(false);

        userService.update(user);
        verify(userDAO).update(any(User.class));
    }

    @Test
    void updateWhenUsernameIsNull(){
        User user = new User();
        user.setPassword("plamen123");
        user.setEmail("plamen@example.com");
        user.setId(1);

        when(userDAO.existsById(any(User.class))).thenReturn(true);
        when(userDAO.existsByEmail(any(User.class))).thenReturn(false);

        userService.update(user);
        verify(userDAO, never()).existsByUsername(any(User.class));
        verify(userDAO).update(any(User.class));
    }

    @Test
    void deleteWhenUserDoesNotExist() {
        when(userDAO.existsById(any(User.class))).thenReturn(false);
        User user = new User();
        user.setId(1);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.delete(user));
        assertEquals("User ID: 1 not found!", exception.getMessage());
        verify(userDAO, never()).delete(any(User.class));
    }

    @Test
    void deleteWhenUserExists() {
        when(userDAO.existsById(any(User.class))).thenReturn(true);
        User user = new User();
        user.setId(1);
        userService.delete(user);
        verify(userDAO, times(1)).delete(any(User.class));
    }

    @Test
    void existsById() {
        when(userDAO.existsById(any(User.class))).thenReturn(true);
        assertTrue(userService.existsById(new User()));
    }
}