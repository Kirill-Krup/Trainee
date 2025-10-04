package com.actisys.userservice.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final Key key;
  private final long accessTokenExpireMillis;
  private final long refreshTokenExpireMillis;

  public JwtUtil(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-expiration-minutes}") long accessTokenExpire,
      @Value("${jwt.refresh-expiration-days}") long refreshTokenExpire
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.accessTokenExpireMillis = accessTokenExpire * 60 * 1000;
    this.refreshTokenExpireMillis = refreshTokenExpire * 24 * 60 * 60 * 1000;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String extractLogin(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }}
