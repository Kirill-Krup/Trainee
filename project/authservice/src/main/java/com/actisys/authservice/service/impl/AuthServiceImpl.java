package com.actisys.authservice.service.impl;

import com.actisys.authservice.dto.AuthRequest;
import com.actisys.authservice.dto.JwtResponse;
import com.actisys.authservice.exception.UserAlreadyExistsException;
import com.actisys.authservice.model.UserCredentials;
import com.actisys.authservice.repository.UserCredentialsRepository;
import com.actisys.authservice.service.AuthService;
import com.actisys.authservice.util.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserCredentialsRepository userCredentialsRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Override
  public JwtResponse register(AuthRequest authRequest) {
    if (userCredentialsRepository.existsById(authRequest.getLogin())) {
      throw new UserAlreadyExistsException(authRequest.getLogin());
    }

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setLogin(authRequest.getLogin());
    userCredentials.setHashedPassword(passwordEncoder.encode(authRequest.getPassword()));
    userCredentialsRepository.save(userCredentials);

    String accessToken = jwtUtil.generateAccessToken(authRequest.getLogin());
    String refreshToken = jwtUtil.generateRefreshToken(authRequest.getLogin());
    userCredentials.setRefreshToken(refreshToken);
    userCredentialsRepository.save(userCredentials);

    return new JwtResponse(accessToken, refreshToken);
  }

  @Override
  public JwtResponse login(AuthRequest authRequest) {
    Optional<UserCredentials> userCredentials = userCredentialsRepository.findById(authRequest.getLogin());
    if (userCredentials.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(),
        userCredentials.get().getHashedPassword())) {
      throw new BadCredentialsException("Invalid login or password");
    }

    String accessToken = jwtUtil.generateAccessToken(authRequest.getLogin());
    String refreshToken = jwtUtil.generateRefreshToken(authRequest.getLogin());

    UserCredentials userCredential = userCredentials.get();
    userCredential.setRefreshToken(refreshToken);
    userCredentialsRepository.save(userCredential);
    return new JwtResponse(accessToken, refreshToken);
  }

  @Override
  public JwtResponse refreshToken(String refreshToken) {
    if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
      throw new BadCredentialsException("Invalid refresh token");
    }

    String login = jwtUtil.extractLogin(refreshToken);

    UserCredentials userCredentials = userCredentialsRepository.findById(login).orElseThrow(()->new BadCredentialsException("User not found"));

    String newAccessToken = jwtUtil.generateAccessToken(login);
    String newRefreshToken = jwtUtil.generateRefreshToken(login);

    userCredentials.setRefreshToken(newRefreshToken);
    userCredentialsRepository.save(userCredentials);

    return new JwtResponse(newAccessToken, newRefreshToken);
  }

  @Override
  public String validateToken(String token) {
    if (token == null || !jwtUtil.validateToken(token)) {
      throw new BadCredentialsException("Invalid token");
    }

    return jwtUtil.extractLogin(token);
  }
}
