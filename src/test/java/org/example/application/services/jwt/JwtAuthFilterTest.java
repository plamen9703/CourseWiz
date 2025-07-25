package org.example.application.services.jwt;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.lang.reflect.Method;

class JwtAuthFilterTest {

//    private JwtAuthFilter filter;
//    private ContainerRequestContext requestContext;
//    private ResourceInfo resourceInfo;
//
//    @BeforeEach
//    void setup() {
//        filter = new JwtAuthFilter(mock(JwtAuthenticator.class),mock(RoleAuthorizer.class));
//        requestContext = mock(ContainerRequestContext.class);
//        resourceInfo = mock(ResourceInfo.class);
//        filter.resourceInfo = resourceInfo;
//    }
//
//    @Test
//    void shouldSkip_WhenNoAuthAnnotation() throws IOException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("noAuthMethod"));
//        filter.filter(requestContext);
//        verify(requestContext, never()).abortWith(any());
//    }
//
//    @Test
//    void shouldSkip_WhenPermitAll() throws IOException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("permitAllMethod"));
//        filter.filter(requestContext);
//        verify(requestContext, never()).abortWith(any());
//    }
//
//    @Test
//    void shouldAbort_WhenDenyAll() throws IOException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("denyAllMethod"));
//        filter.filter(requestContext);
//        verify(requestContext).abortWith(argThat(response ->
//                response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()));
//    }

//    @Test
//    void shouldAbort_WhenMissingAuthHeader() throws IOException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("authMethod"));
//        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);
//
//        filter.filter(requestContext);
//
//        verify(requestContext).abortWith(argThat(response ->
//                response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode() &&
//                ((Map<?, ?>) response.getEntity()).get("message").equals("Missing or invalid Authorization header")));
//    }

//    @Test
//    void shouldAbort_WhenInvalidToken() throws IOException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("authMethod"));
//        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalidToken");
//
//        try (MockedStatic<JwtAuthenticator> jwtMock = Mockito.mockStatic(JwtAuthenticator.class)) {
//            JwtAuthenticator authenticator = mock(JwtAuthenticator.class);
//            jwtMock.when(JwtAuthenticator::new).thenReturn(authenticator);
//            when(authenticator.authenticate(anyString())).thenReturn(Optional.empty());
//
//            assertThrows(UserTokenInvalidException.class, () -> filter.filter(requestContext));
//        } catch (AuthenticationException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Test
//    void shouldSetSecurityContext_WhenValidTokenAndNoRoles() throws IOException, AuthenticationException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("authMethod"));
//        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
//        when(requestContext.getSecurityContext()).thenReturn(mock(javax.ws.rs.core.SecurityContext.class));
//
//        UserAuthenticated user = new UserAuthenticated(1, "john", "john@example.com", Set.of("USER"), Set.of());
//
//        try (MockedStatic<JwtAuthenticator> jwtMock = Mockito.mockStatic(JwtAuthenticator.class)) {
//            JwtAuthenticator authenticator = mock(JwtAuthenticator.class);
//            RoleAuthorizer authorizer = mock(RoleAuthorizer.class);
//            filter = new JwtAuthFilter(authenticator, authorizer);
//
//            when(resourceInfo.getResourceMethod()).thenReturn(getMethod("authMethod"));
//            when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalidToken");
//            when(authenticator.authenticate(anyString())).thenReturn(Optional.empty());
//
//            assertThrows(UserTokenInvalidException.class, () -> filter.filter(requestContext));
//
//
//            verify(requestContext).setSecurityContext(any(JwtSecurityContext.class));
//        }
//    }

//    @Test
//    void shouldAbort_WhenRolesAllowedAndNotAuthorized() throws IOException {
//        when(resourceInfo.getResourceMethod()).thenReturn(getMethod("rolesAllowedMethod"));
//        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
//        when(requestContext.getSecurityContext()).thenReturn(mock(javax.ws.rs.core.SecurityContext.class));
//
//        UserAuthenticated user = new UserAuthenticated(1, "john", "john@example.com", Set.of("USER"), Set.of());
//
//        try (MockedStatic<JwtAuthenticator> jwtMock = Mockito.mockStatic(JwtAuthenticator.class)) {
//            JwtAuthenticator authenticator = mock(JwtAuthenticator.class);
//            jwtMock.when(JwtAuthenticator::new).thenReturn(authenticator);
//            try {
//                when(authenticator.authenticate(anyString())).thenReturn(Optional.of(user));
//            } catch (AuthenticationException e) {
//                throw new RuntimeException(e);
//            }
//
//            try (MockedStatic<RoleAuthorizer> roleMock = Mockito.mockStatic(RoleAuthorizer.class)) {
//                RoleAuthorizer authorizer = mock(RoleAuthorizer.class);
//                roleMock.when(RoleAuthorizer::new).thenReturn(authorizer);
//                when(authorizer.authorize(user, "ADMIN")).thenReturn(false);
//
//                filter.filter(requestContext);
//
//                verify(requestContext).abortWith(argThat(response ->
//                        response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()));
//            }
//        }
//    }
    // Dummy methods for annotations
    @NoAuth void noAuthMethod() {}
    @PermitAll
    void permitAllMethod() {}
    @DenyAll
    void denyAllMethod() {}
    void authMethod() {}
    @RolesAllowed({"ADMIN"}) void rolesAllowedMethod() {}

    private Method getMethod(String name) {
        try {
            return this.getClass().getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}