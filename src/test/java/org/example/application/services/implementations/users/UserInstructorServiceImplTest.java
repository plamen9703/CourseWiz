package org.example.application.services.implementations.users;

class UserInstructorServiceImplTest {
//
//    private final UserInstructorDAO userInstructorDAO = mock(UserInstructorDAO.class);
//
//    private UserInstructorServiceImpl userInstructorService;
//
//    @BeforeEach
//    void setUp() {
//        userInstructorService = new UserInstructorServiceImpl(userInstructorDAO);
//    }
//
//    @AfterEach
//    void tearDown() {
//        reset(userInstructorDAO);
//    }
//
//    @Test
//    void findAll() {
//        when(userInstructorDAO.findAll()).thenReturn(List.of());
//        assertEquals(0, userInstructorService.findAll().size());
//    }
//
//    @Test
//    void findByIdWhenUserDoesNotExist() {
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        when(userInstructorDAO.findById(any(UserInstructor.class))).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.findById(userInstructor));
//        assertEquals("User ID: 1 not found!", exception.getMessage());
//    }
//
//    @Test
//    void findByIdWhenUserDoesExist() {
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//
//        UserInstructor userInstructorReturned = new UserInstructor();
//        userInstructorReturned.setId(1);
//
//        when(userInstructorDAO.findById(any(UserInstructor.class))).thenReturn(Optional.of(userInstructorReturned));
//        UserInstructor found = userInstructorService.findById(userInstructor);
//        assertEquals(userInstructor, found);
//    }
//
//    @Test
//    void createWhenEmailExists() {
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(true);
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setUsername("plamen");
//        userInstructor.setEmail("plamen@example.com");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        assertEquals("User with identifier: plamen@example.com already exists.", exception.getMessage());
//        verify(userInstructorDAO, never()).insert(any(UserInstructor.class));
//    }
//
//    @Test
//    void createWhenUsernameExists() {
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(true);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setUsername("plamen");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        assertEquals("User with identifier: plamen already exists.", exception.getMessage());
//        verify(userInstructorDAO, never()).insert(any(UserInstructor.class));
//    }
//
//    @Test
//    void createWhenUsernameIsNull() {
//        UserInstructor userInstructor = new UserInstructor();
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        assertEquals("Invalid user username! Username: null", exception.getMessage());
//        verify(userInstructorDAO, never()).insert(any(UserInstructor.class));
//    }
//
//    @Test
//    void createWhenEmailIsNull() {
//        UserInstructor userInstructor = new UserInstructor();
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//        userInstructor.setUsername("plamen");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        assertEquals("User email is not valid. Email: null", exception.getMessage());
//        verify(userInstructorDAO, never()).insert(any(UserInstructor.class));
//    }
//    @Test
//    void createWhenEmailIsNotValid() {
//        UserInstructor userInstructor = new UserInstructor();
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//        userInstructor.setUsername("plamen");
//        userInstructor.setEmail("plamen@example");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        assertEquals("User email is not valid. Email: plamen@example", exception.getMessage());
//        verify(userInstructorDAO, never()).insert(any(UserInstructor.class));
//    }
//
//    @Test
//    void createWhenPasswordIsEmpty() {
//        UserInstructor userInstructor = spy(new UserInstructor());
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(false);
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//        userInstructor.setUsername("plamen");
//        userInstructor.setEmail("plamen@gmail.com");
//        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        userInstructor.setPassword("");
//        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> userInstructorService.create(userInstructor));
//        assertEquals("User password can not be empty.", exception1.getMessage());
//        assertEquals("User password can not be empty.", exception2.getMessage());
//        verify(userInstructor, times(1)).setPassword(anyString());
//        verify(userInstructorDAO, never()).insert(any(UserInstructor.class));
//    }
//
//    @Test
//    void createWhenHappyCase() {
//        UserInstructor userInstructor = spy(new UserInstructor());
//        userInstructor.setEmail("plamen@gmail.com");
//        userInstructor.setUsername("plamen");
//        userInstructor.setPassword("plamen123");
//
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(false);
//        UserInstructor userInstructorReturned = new UserInstructor();
//        userInstructorReturned.setEmail("plamen@example.com");
//        userInstructorReturned.setUsername("plamen");
//        when(userInstructorDAO.insert(userInstructor)).thenReturn(userInstructorReturned);
//
//        UserInstructor created = userInstructorService.create(userInstructor);
//
//        assertNotNull(created);
//        assertEquals(userInstructorReturned.getEmail(), created.getEmail());
//        assertEquals(userInstructorReturned.getUsername(), created.getUsername());
//        assertNull(created.getPassword());
//
//
//        verify(userInstructor, times(2)).setPassword(anyString());
//        verify(userInstructorDAO).insert(any(UserInstructor.class));
//    }
//
//    @Test
//    void updateWhenUserDoesNotExist() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(false);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        userInstructor.setUsername("");
//        userInstructor.setPassword("");
//        userInstructor.setEmail("");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.update(userInstructor));
//        assertEquals("User ID: 1 not found!", exception.getMessage());
//        verify(userInstructorDAO, never()).update(any(UserInstructor.class));
//    }
//
//
//    @Test
//    void updateWhenUserExistsAndNewEmailIsNotValid() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        userInstructor.setEmail("plamen");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.update(userInstructor));
//        assertEquals("User email is not valid. Email: plamen", exception.getMessage());
//        verify(userInstructorDAO, times(0)).update(any(UserInstructor.class));
//    }
//
//    @Test
//    void updateWhenUserExistsAndNewEmailIsTaken() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(true);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        userInstructor.setEmail("plamen@example.com");
//        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> userInstructorService.update(userInstructor));
//        assertEquals("User with identifier: plamen@example.com already exists.", exception.getMessage());
//        verify(userInstructorDAO, times(0)).update(any(UserInstructor.class));
//    }
//
//    @Test
//    void updateWhenUserExistsAndNewUsernameIsTaken() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(true);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        userInstructor.setUsername("plamen");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.update(userInstructor));
//        assertEquals("User with identifier: plamen already exists.", exception.getMessage());
//        verify(userInstructorDAO, times(0)).update(any(UserInstructor.class));
//    }
//
//    @Test
//    void updateWhenUserExistsAndNewUsernameAndEmailIsValidAndPasswordIsNullOrEmpty() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(false);
//        UserInstructor userInstructor = spy(new UserInstructor());
//        userInstructor.setId(1);
//        userInstructor.setUsername("plamen");
//        userInstructor.setEmail("plamen@gmail.com");
//        userInstructorService.update(userInstructor);
//        userInstructor.setPassword("");
//        userInstructorService.update(userInstructor);
//        verify(userInstructor, times(1)).setPassword(anyString());
//        verify(userInstructorDAO, times(2)).update(any(UserInstructor.class));
//    }
//
//    @Test
//    void updateWhenUserNewUsernameEmailPasswordAreNull() {
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructorService.update(userInstructor);
//        verify(userInstructorDAO, never()).existsByEmail(any(UserInstructor.class));
//        verify(userInstructorDAO, never()).existsByUsername(any(UserInstructor.class));
//        verify(userInstructorDAO, never()).existsById(any(UserInstructor.class));
//        verify(userInstructorDAO, never()).update(any(UserInstructor.class));
//
//    }
//
//    @Test
//    void updateWhenPasswordIsNotNullOrEmpty(){
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setPassword("plamen123");
//        userInstructor.setUsername("plamen");
//        userInstructor.setEmail("plamen@example.com");
//        userInstructor.setId(1);
//
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(false);
//        when(userInstructorDAO.existsByUsername(any(UserInstructor.class))).thenReturn(false);
//
//        userInstructorService.update(userInstructor);
//        verify(userInstructorDAO).update(any(UserInstructor.class));
//    }
//
//    @Test
//    void updateWhenUsernameIsNull(){
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setPassword("plamen123");
//        userInstructor.setEmail("plamen@example.com");
//        userInstructor.setId(1);
//
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        when(userInstructorDAO.existsByEmail(any(UserInstructor.class))).thenReturn(false);
//
//        userInstructorService.update(userInstructor);
//        verify(userInstructorDAO, never()).existsByUsername(any(UserInstructor.class));
//        verify(userInstructorDAO).update(any(UserInstructor.class));
//    }
//
//    @Test
//    void deleteWhenUserDoesNotExist() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(false);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.delete(userInstructor));
//        assertEquals("User ID: 1 not found!", exception.getMessage());
//        verify(userInstructorDAO, never()).delete(any(UserInstructor.class));
//    }
//
//    @Test
//    void deleteWhenUserExists() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setId(1);
//        userInstructorService.delete(userInstructor);
//        verify(userInstructorDAO, times(1)).delete(any(UserInstructor.class));
//    }
//
//    @Test
//    void existsById() {
//        when(userInstructorDAO.existsById(any(UserInstructor.class))).thenReturn(true);
//        assertTrue(userInstructorService.existsById(new UserInstructor()));
//    }
//
//    @Test
//    void loginWhenPasswordIsNullOrEmpty() {
//        UserInstructor userInstructor = new UserInstructor();
//        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> userInstructorService.login(userInstructor));
//        userInstructor.setPassword("");
//        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> userInstructorService.login(userInstructor));
//        assertEquals("User password can not be empty.",exception1.getMessage());
//        assertEquals("User password can not be empty.",exception2.getMessage());
//
//    }
//
//    @Test
//    void loginWhenUsernameAndEmailAreNull() {
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setPassword("plamen123");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.login(userInstructor));
//        assertEquals("Missing user identifier username or email.",exception.getMessage());
//
//    }
//
//    @Test
//    void loginWhenUserPasswordDoesNotMatch() {
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setUsername("plamen");
//        userInstructor.setPassword("plamen12");
//        UserInstructor userInstructorReturned = new UserInstructor();
//        userInstructorReturned.setPassword(BCrypt.hashpw("plamen123", BCrypt.gensalt()));
//        when(userInstructorDAO.findByUsername(any(UserInstructor.class))).thenReturn(Optional.of(userInstructorReturned));
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.login(userInstructor));
//        assertEquals("User password doesn't match!",exception.getMessage());
//
//    }
//
//    @Test
//    void loginWhenUserPasswordDoMatch() {
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setUsername("plamen");
//        userInstructor.setPassword("plamen123");
//
//        String tokenExpected = JwtUtil.generateToken(userInstructor);
//
//        UserInstructor userInstructorReturned = new UserInstructor();
//        userInstructorReturned.setPassword(BCrypt.hashpw("plamen123", BCrypt.gensalt()));
//
//        when(userInstructorDAO.findByUsername(any(UserInstructor.class))).thenReturn(Optional.of(userInstructorReturned));
//        String token = userInstructorService.login(userInstructor);
//
//
//        assertNotNull(token);
//        assertFalse(token.isEmpty());
//        assertTrue(token.startsWith("ey"));
//    }
//
//
//
//    @Test
//    void findByUsernameWithEmptyOptional() {
//        when(userInstructorDAO.findByUsername(any(UserInstructor.class))).thenReturn(Optional.empty());
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setUsername("plamen");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.findByUsername(userInstructor));
//        assertEquals("User with identifier: plamen not found.", exception.getMessage());
//    }
//
//    @Test
//    void findByEmailWithEmptyOptional() {
//        when(userInstructorDAO.findByEmail(any(UserInstructor.class))).thenReturn(Optional.empty());
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setEmail("plamen@example.com");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.findByEmail(userInstructor));
//        assertEquals("User with identifier: plamen@example.com not found.", exception.getMessage());
//    }
//
//    @Test
//    void findByEmailWithInvalidEmail() {
//        UserInstructor userInstructor=new UserInstructor();
//        userInstructor.setEmail("plamen@example");
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.findByEmail(userInstructor));
//        assertEquals("User email is not valid. Email: plamen@example", exception.getMessage());
//    }
//
//    @Test
//    void findByUsernameOrEmailWhenExceptionIsThrown() {
//        UserInstructor UserInstructor = new UserInstructor();
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userInstructorService.findByUsernameOrEmail(UserInstructor));
//        assertEquals("Missing user identifier username or email.", exception.getMessage());
//        verify(userInstructorDAO, never()).findByEmail(any(UserInstructor.class));
//        verify(userInstructorDAO, never()).findByUsername(any(UserInstructor.class));
//    }
//
//    @Test
//    void findByUsernameOrEmailWithEmail() {
//        UserInstructor UserInstructor = new UserInstructor();
//        UserInstructor.setEmail("plamen@example.com");
//
//        UserInstructor userInstructor = new UserInstructor();
//        userInstructor.setUsername("plamen");
//        userInstructor.setEmail("plamen@example.com");
//        when(userInstructorDAO.findByEmail(UserInstructor)).thenReturn(Optional.of(userInstructor));
//        UserInstructor found = userInstructorService.findByUsernameOrEmail(UserInstructor);
//        assertEquals(userInstructor, found);
//        verify(userInstructorDAO, times(1)).findByEmail(any(UserInstructor.class));
//    }
//
//    @Test
//    void findByUsernameOrEmailWithUsername() {
//        UserInstructor UserInstructor = new UserInstructor();
//        UserInstructor.setUsername("plamen");
//
//        UserInstructor UserInstructorReturned = new UserInstructor();
//        UserInstructorReturned.setUsername("plamen");
//        UserInstructorReturned.setEmail("plamen@example.com");
//        when(userInstructorDAO.findByUsername(UserInstructor)).thenReturn(Optional.of(UserInstructorReturned));
//        UserInstructor found = userInstructorService.findByUsernameOrEmail(UserInstructor);
//        assertEquals(UserInstructorReturned, found);
//        verify(userInstructorDAO, times(1)).findByUsername(any(UserInstructor.class));
//    }
}