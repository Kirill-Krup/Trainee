package com.actisys.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class JwtResponse {

  private final String accessToken;
  private final String refreshToken;
}