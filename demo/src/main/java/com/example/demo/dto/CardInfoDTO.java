package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CardInfoDTO {

  private final Long id;

  @NotBlank(message = "Number is required")
  private final String number;

  @NotBlank(message = "Holder is required")
  private final String holder;

  private final Timestamp expirationDate;

}
