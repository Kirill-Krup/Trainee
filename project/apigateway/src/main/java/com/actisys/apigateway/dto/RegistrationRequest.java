package com.actisys.apigateway.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegistrationRequest {

  private final AuthRequest authRequest;
  private final UserDTO userDTO;

}
