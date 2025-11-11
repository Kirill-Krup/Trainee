package com.actisys.apigateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {

  @NotBlank(message = "Login is required")
  @Size(min = 3, max = 50, message = "Login should be over 3 and smaller than 50 symbols")
  private String login;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password should be over 8 symbols")
  private String password;
}
