package com.actisys.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class CreateCardInfoDTO {

  @NotBlank(message = "Number is required")
  private final String number;

  @NotBlank(message = "Holder is required")
  private final String holder;

  private final Timestamp expirationDate;
}
