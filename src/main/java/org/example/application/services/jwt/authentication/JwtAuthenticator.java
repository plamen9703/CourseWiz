package org.example.application.services.jwt.authentication;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.PrematureJwtException;
import org.example.application.api.users.UserAuthenticated;
import org.example.application.services.jwt.JwtUtil;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticator implements Authenticator<String, UserAuthenticated> {


    @Override
    public Optional<UserAuthenticated> authenticate(String token) throws AuthenticationException {
        UserAuthenticated user;
        Claims claims;
        try{
            claims = JwtUtil.validaToken(token);
            Instant issuredAt=claims.getIssuedAt().toInstant();
            Instant expiredAt=claims.getExpiration().toInstant();
            if (Instant.now().isBefore(issuredAt)) {
                throw new PrematureJwtException(null,claims,"Token is not VALID yet!");
            }

            if(Instant.now().isAfter(expiredAt)){
                throw new ExpiredJwtException(null, claims, "Token has expired!");
            }


            Integer id = Integer.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            String email = claims.get("email", String.class);
            List<String> roles = claims.get("roles", List.class);
            List<String> permissions = claims.get("permissions", List.class);

            user = new UserAuthenticated(id, username, email, new HashSet<>(roles), new HashSet<>(permissions));
            return Optional.of(user);

        }catch (Exception e) {
            return Optional.empty();
        }
    }
}
