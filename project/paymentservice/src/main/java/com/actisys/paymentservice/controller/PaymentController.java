package com.actisys.paymentservice.controller;

import com.actisys.paymentservice.dto.PaymentCreateDTO;
import com.actisys.paymentservice.dto.PaymentDTO;
import com.actisys.paymentservice.model.enums.PaymentStatus;
import com.actisys.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/createPayment")
  public ResponseEntity<PaymentDTO> createPayment(@RequestBody @Valid PaymentCreateDTO paymentDTO) {
    return ResponseEntity.ok(paymentService.createPayment(paymentDTO));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable String id) {
    PaymentDTO paymentDTO = paymentService.getPaymentById(id);
    return ResponseEntity.ok(paymentDTO);
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<PaymentDTO>> getPaymentByOrderId(@PathVariable Long orderId) {
    List<PaymentDTO> payments = paymentService.getPaymentsByOrderId(orderId);
    return ResponseEntity.ok(payments);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PaymentDTO>> getPaymentByUserId(@PathVariable Long userId) {
    List<PaymentDTO> payments = paymentService.getPaymentsByUserId(userId);
    return ResponseEntity.ok(payments);
  }

  @GetMapping("/status")
  public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(
      @RequestParam List<PaymentStatus> statuses) {
    List<PaymentDTO> payments = paymentService.findPaymentsByStatus(statuses);
    return ResponseEntity.ok(payments);
  }

  @GetMapping("/summary/period")
  public ResponseEntity<Double> getTotalSumForPeriod(
      @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
      @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
    Double totalSum = paymentService.getTotalSumForPeriod(from, to);
    return ResponseEntity.ok(totalSum);
  }

  @DeleteMapping("/deletePayment/{id}")
  public ResponseEntity<Void> deletePayment(@PathVariable String id) {
    paymentService.deletePayment(id);
    return ResponseEntity.ok().build();
  }
}
