package com.actisys.orderservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreatePaymentEvent {
  private String paymentId;
  private Long orderId;
  private String status;
}
