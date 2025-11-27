package com.actisys.orderservice.dto.event;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderEvent {
  private Long orderId;
  private Long userId;
  private BigDecimal amount;
}
