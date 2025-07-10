package org.example.application.services;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtService {

    private String secretkey;

    public JwtService(String key) {
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey=keyGenerator.generateKey();
            this.secretkey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username){
//        Map<String, Object> claims=new HashMap<>();
//        return Jwts.builder()
//                .claims()
//                .add(claims)
//                .subject(username)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
//                .and()
//                .signWith(getKey())
//                .compact();
        return null;
    }
    private Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
//        return Keys.hmacShaKeyFor(keyBytes);
        return null;
    }
}
