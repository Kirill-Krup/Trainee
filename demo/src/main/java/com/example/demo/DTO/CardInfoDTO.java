package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class CardInfoDTO {

  private Long id;

  @NotBlank(message = "Number is required")
  private String number;

  @NotBlank(message = "Holder is required")
  private String holder;

  private Timestamp expirationDate;

}
