package org.example.application.services.auth.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.application.api.User;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public abstract class JwtService {
    

    private String key="";

    public JwtService() {
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey=keyGenerator.generateKey();
            key= Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public  String generateToken(User user){
        Map<String, Object> claims=new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("roles",user.getRoles().toArray());
        claims.put("permissions", user.getPermissions());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+10*60*1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    public  SecretKey getKey() {
        byte[] keyBytes = Base64
                .getUrlDecoder()
                .decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
