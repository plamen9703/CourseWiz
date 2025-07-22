package org.example.application.services.jwt.authentication;

import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.Claims;
import org.example.application.api.users.UserAuthenticated;
import org.example.application.services.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticatorTest {

    private JwtAuthenticator jwtAuthenticator;

    @BeforeEach
    void setUp() {
        jwtAuthenticator = new JwtAuthenticator();
    }

    @Test
    void authenticate_ShouldReturnUser_WhenTokenIsValid() {
        // Arrange
        String token = "validToken";
        Claims claims = mock(Claims.class);

        when(claims.getIssuedAt()).thenReturn(Date.from(Instant.now().minusSeconds(60)));
        when(claims.getExpiration()).thenReturn(Date.from(Instant.now().plusSeconds(3600)));
        when(claims.getSubject()).thenReturn("123");
        when(claims.get("username", String.class)).thenReturn("john");
        when(claims.get("email", String.class)).thenReturn("john@example.com");
        when(claims.get("roles", List.class)).thenReturn(Arrays.asList("ADMIN", "USER"));
        when(claims.get("permissions", List.class)).thenReturn(Arrays.asList("READ", "WRITE"));

        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.validaToken(token)).thenReturn(claims);

            // Act
            Optional<UserAuthenticated> result = jwtAuthenticator.authenticate(token);

            // Assert
            assertTrue(result.isPresent());
            UserAuthenticated user = result.get();
            assertEquals(123, user.getId());
            assertEquals("john", user.getUsername());
            assertEquals("john@example.com", user.getEmail());
            assertTrue(user.getRoles().contains("ADMIN"));
            assertTrue(user.getPermissions().contains("WRITE"));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void authenticate_ShouldReturnEmpty_WhenTokenIssuedInFuture() {
        // Arrange
        String token = "futureToken";
        Claims claims = mock(Claims.class);

        when(claims.getIssuedAt()).thenReturn(Date.from(Instant.now().plusSeconds(3600))); // future
        when(claims.getExpiration()).thenReturn(Date.from(Instant.now().plusSeconds(7200)));

        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.validaToken(token)).thenReturn(claims);

            // Act
            Optional<UserAuthenticated> result = jwtAuthenticator.authenticate(token);

            // Assert
            assertFalse(result.isPresent());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void authenticate_ShouldReturnEmpty_WhenTokenExpired() {
        // Arrange
        String token = "expiredToken";
        Claims claims = mock(Claims.class);

        when(claims.getIssuedAt()).thenReturn(Date.from(Instant.now().minusSeconds(7200)));
        when(claims.getExpiration()).thenReturn(Date.from(Instant.now().minusSeconds(3600))); // already expired

        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.validaToken(token)).thenReturn(claims);

            // Act
            Optional<UserAuthenticated> result = jwtAuthenticator.authenticate(token);

            // Assert
            assertFalse(result.isPresent());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void authenticate_ShouldReturnEmpty_WhenJwtUtilThrowsException() {
        // Arrange
        String token = "invalidToken";

        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.validaToken(token))
                    .thenThrow(new RuntimeException("Invalid token"));

            // Act
            Optional<UserAuthenticated> result = jwtAuthenticator.authenticate(token);

            // Assert
            assertFalse(result.isPresent());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }
}