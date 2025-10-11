package com.actisys.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemDTO {

  private final Long id;

  @NotBlank(message = "Name for item is required")
  private final String name;

  @NotNull(message = "Price is required")
  @Positive(message = "Price must be positive value")
  private final BigDecimal price;
}
