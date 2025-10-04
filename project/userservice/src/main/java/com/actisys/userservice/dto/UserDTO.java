package com.actisys.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
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

  private final List<CardInfoDTO> cards;
}
