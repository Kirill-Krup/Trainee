package com.actisys.paymentservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PaymentCreateDTO {
  @NotNull(message = "OrderId cannot be null")
  @Positive(message = "OrderId must be positive")
  private final Long orderId;

  @NotNull(message = "UserId cannot be null")
  @Positive(message = "UserId must be positive")
  private final Long userId;

  @NotNull(message = "Payment amount is required")
  @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
  private final BigDecimal paymentAmount;
}
