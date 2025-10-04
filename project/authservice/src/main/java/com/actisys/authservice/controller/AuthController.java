package com.actisys.authservice.controller;

import com.actisys.authservice.dto.AuthRequest;
import com.actisys.authservice.dto.JwtResponse;
import com.actisys.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<JwtResponse> register(@RequestBody AuthRequest authRequest) {
    JwtResponse response = authService.register(authRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody AuthRequest authRequest) {
    JwtResponse response = authService.login(authRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<JwtResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
    JwtResponse response = authService.refreshToken(refreshToken);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/validate")
  public ResponseEntity<String> validate(@RequestParam("token") String token) {
    String login = authService.validateToken(token);
    return ResponseEntity.ok(login);
  }
}
