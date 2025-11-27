package com.actisys.apigateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class JwtAuthFilter implements GlobalFilter {

  private final SecretKey key;

  private static final List<String> PUBLIC_PATHS = List.of(
      "/api/v1/register",
      "/api/v1/auth/login",
      "/api/v1/auth/register",
      "/api/v1/auth/refresh"
  );

  public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();

    if (PUBLIC_PATHS.stream().anyMatch(path::equals)) {
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return onError(exchange, "Missing token", HttpStatus.UNAUTHORIZED);
    }

    try {
      String token = authHeader.substring(7);
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return chain.filter(exchange);
    } catch (Exception e) {
      return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
    }
  }

  private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus status) {
    exchange.getResponse().setStatusCode(status);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    String json = "{\"error\": \"" + msg + "\"}";
    return exchange.getResponse()
        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(json.getBytes())));
  }
}