package com.actisys.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class CreateCardInfoDTO {

  @NotBlank(message = "Number is required")
  private final String number;

  @NotNull(message = "User id is required")
  private final Long userId;

  @NotBlank(message = "Holder is required")
  private final String holder;

  private final Timestamp expirationDate;
}
