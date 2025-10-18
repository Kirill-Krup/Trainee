package com.actisys.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {

  @NotBlank(message = "Логин не может быть пустым")
  @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
  private String login;

  @NotBlank(message = "Пароль не может быть пустым")
  @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
  private String password;
}
