package com.actisys.paymentservice.service;

import com.actisys.paymentservice.dto.PaymentCreateDTO;
import com.actisys.paymentservice.dto.PaymentDTO;
import com.actisys.paymentservice.model.enums.PaymentStatus;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;

public interface PaymentService {

  PaymentDTO createPayment(@Valid PaymentCreateDTO paymentDTO);

  void deletePayment(String id);

  PaymentDTO getPaymentById(String id);

  List<PaymentDTO> getPaymentsByOrderId(Long orderId);

  List<PaymentDTO> getPaymentsByUserId(Long userId);

  List<PaymentDTO> findPaymentsByStatus(List<PaymentStatus> statuses);

  Double getTotalSumForPeriod(Instant from, Instant to);
}
