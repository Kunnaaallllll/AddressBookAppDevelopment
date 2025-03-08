package com.example.AddressBookApp.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtility {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //Generate Token
    public String generateToken(String email){
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(secretKey).compact();
    }

    //Extract Email From Token
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Extract Any Claims
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        return  claimsResolver.apply(claims);
    }

    // Validate Token
    public boolean validateToken(String token, String userEmail) {
        final String email = extractEmail(token);
        return (email.equals(userEmail) && !isTokenExpired(token));
    }

    // Check Expiration
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}