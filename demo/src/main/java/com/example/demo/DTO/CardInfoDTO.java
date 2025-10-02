package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class CardInfoDTO {

  private final Long id;

  @NotBlank(message = "Number is required")
  private final String number;

  @NotBlank(message = "Holder is required")
  private final String holder;

  private final Timestamp expirationDate;

}
