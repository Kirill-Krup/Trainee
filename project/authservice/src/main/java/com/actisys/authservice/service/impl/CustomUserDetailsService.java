package com.actisys.authservice.service.impl;

import com.actisys.authservice.model.UserCredentials;
import com.actisys.authservice.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserCredentialsRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserCredentials user = userRepository.findByLogin(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getLogin())
        .password(user.getHashedPassword())
        .roles("USER")
        .build();
  }
}

