package com.actisys.orderservice.consumer;

import com.actisys.orderservice.dto.event.CreatePaymentEvent;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {
  private final OrderService orderService;

  @KafkaListener(topics = "CREATE_PAYMENT", groupId = "order-service-group")
  public void handleCreatePayment(CreatePaymentEvent event) {
    StatusType status = "SUCCESS".equals(event.getStatus())
        ? StatusType.PAID : StatusType.PAYMENT_FAILED;
    orderService.updateOrderStatus(event.getOrderId(), status);
  }
}
