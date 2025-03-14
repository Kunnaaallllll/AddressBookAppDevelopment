package com.example.AddressBookApp.utility;

import com.example.AddressBookApp.model.User;
import com.example.AddressBookApp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtility {

    @Autowired
    UserRepository userRepository;

    private static final String SECRET_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789ABCDEFGHI";


    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY.getBytes())
                .compact();
    }

    public String extractEmail(String token) {
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }
        catch (Exception e){
            //Token expired
            return null;
        }
    }


    public boolean validateToken(String token, String userEmail) {
        final String email = extractEmail(token);
        boolean isTokenPresent = true;
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && user.getToken() ==null){
            isTokenPresent = false;
        }
        final boolean valid = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());

        return (email.equals(userEmail) && !valid && isTokenPresent);
    }
}