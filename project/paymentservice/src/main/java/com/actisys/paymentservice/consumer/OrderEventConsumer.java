package com.actisys.paymentservice.consumer;

import com.actisys.paymentservice.dto.PaymentCreateDTO;
import com.actisys.paymentservice.dto.PaymentDTO;
import com.actisys.paymentservice.dto.event.CreateOrderEvent;
import com.actisys.paymentservice.dto.event.CreatePaymentEvent;
import com.actisys.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
  private final PaymentService paymentService;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @KafkaListener(topics = "CREATE_ORDER", groupId = "payment-service-group")
  public void handleCreateOrder(CreateOrderEvent event){
    log.debug("New event Create_order: {}", event);
    PaymentCreateDTO paymentCreateDTO = new PaymentCreateDTO(
        event.getOrderId(),
        event.getUserId(),
        event.getAmount()
    );
    PaymentDTO paymentDTO = paymentService.createPayment(paymentCreateDTO);
    CreatePaymentEvent paymentEvent = CreatePaymentEvent.builder()
        .paymentId(paymentDTO.getId())
        .orderId(paymentDTO.getOrderId())
        .status(paymentDTO.getStatus().toString())
        .build();
    kafkaTemplate.send("CREATE_PAYMENT", paymentEvent);
  }
}
