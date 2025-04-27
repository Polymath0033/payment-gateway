package com.polymath.payment_gateway.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expiration}")
    private long accessToken;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshToken;

    public String generateAccessToken(String subject) {
        return generateToken(subject,accessToken);
    }
    public String generateRefreshToken(String subject) {
        return generateToken(subject,refreshToken);
    }
    private String generateToken(String subject, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .and()
                .signWith(getSecretKey()).compact();
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        LocalDateTime expirationDateTime = expirationDate(token);
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return expirationDateTime.isBefore(now);
    }



    private LocalDateTime expirationDate(String token) {
        Date expirationDate = extractClaim(token,Claims::getExpiration);
        return convertDateToLocalTime(expirationDate);
    }

    private LocalDateTime convertDateToLocalTime(Date expirationDate) {
        return LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey getSecretKey(){
        byte[] encodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }


}
