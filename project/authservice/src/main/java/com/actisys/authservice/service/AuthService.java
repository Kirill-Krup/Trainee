package com.actisys.authservice.service;


import com.actisys.authservice.dto.AuthRequest;
import com.actisys.authservice.dto.JwtResponse;

public interface AuthService {

  /**
   * Save new credentials and return Jwt tokens
   * */
  JwtResponse register(AuthRequest authRequest);

  /**
   * Refresh token and return Jwt tokens
   * */
  JwtResponse refreshToken(String refreshToken);

  /**
   * Check token
   * */
  String validateToken(String token);

  /**
   * Method for check user's login and password
   * return two Jwt tokens
   * */
  JwtResponse login(AuthRequest authRequest);

}
