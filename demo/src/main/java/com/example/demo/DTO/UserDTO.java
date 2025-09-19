package com.example.demo.DTO;

import com.example.demo.Model.CardInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

@Data
public class UserDTO {

  private Long id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Surname is required")
  private String surname;

  @NotBlank(message = "Birthday must be in the past")
  private Timestamp birthDate;

  @Email(message = "Email should be valid")
  private String email;

  private List<CardInfoDTO> cards;
}
