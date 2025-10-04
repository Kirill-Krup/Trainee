package com.actisys.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class CardInfoDTO {

  private final Long id;

  @NotBlank(message = "Number is required")
  private final String number;

  @NotBlank(message = "Holder is required")
  private final String holder;

  private final Timestamp expirationDate;

}
