package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class CreateCardInfoDTO {

  @NotBlank(message = "Number is required")
  private final String number;

  @NotBlank(message = "Holder is required")
  private final String holder;

  private final Timestamp expirationDate;
}
