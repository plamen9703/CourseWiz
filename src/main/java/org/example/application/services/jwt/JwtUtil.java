package org.example.application.services.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.application.api.users.User;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtUtil {
    

    private static final Key KEY;
    static {
        try {
            KEY = KeyGenerator.getInstance("HmacSHA256").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
//    public JwtService() {
//        try {
//            KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey=keyGenerator.generateKey();
//            key= Base64.getUrlEncoder()
//                    .withoutPadding()
//                    .encodeToString(secretKey.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public static String generateToken(User user){
        Map<String, Object> claims=new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("roles",user.getRoles().toArray());
        claims.put("permissions", user.getPermissions().toArray());
        Instant issuedAt = Instant.now()
                .truncatedTo(ChronoUnit.SECONDS);
//                .plus(10, ChronoUnit.SECONDS);
        Instant expiration = issuedAt
                .plus(10, ChronoUnit.MINUTES);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .and()
                .signWith(KEY)
                .compact();
    }

//    public  SecretKey getKey() {
//        byte[] keyBytes = Base64
//                .getUrlDecoder()
//                .decode(KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    public static Claims validaToken(String token){
            return Jwts.parser()
                    .verifyWith((SecretKey) KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }


    public static SecretKey getKey(){
        return (SecretKey) KEY;
    }

}
