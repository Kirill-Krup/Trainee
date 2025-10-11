package com.actisys.orderservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {

  private final Long id;

  @NotBlank(message = "Name is required")
  private final String name;

  @NotBlank(message = "Surname is required")
  private final String surname;

  @NotBlank(message = "Birthday must be in the past")
  private final Timestamp birthDate;

  @Email(message = "Email should be valid")
  private final String email;
}
