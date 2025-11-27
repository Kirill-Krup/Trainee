package com.actisys.paymentservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.actisys.paymentservice.client.RandomNumberClient;
import com.actisys.paymentservice.dto.PaymentCreateDTO;
import com.actisys.paymentservice.dto.PaymentDTO;
import com.actisys.paymentservice.exception.PaymentNotFoundException;
import com.actisys.paymentservice.mapper.PaymentMapper;
import com.actisys.paymentservice.model.Payment;
import com.actisys.paymentservice.model.enums.PaymentStatus;
import com.actisys.paymentservice.repository.PaymentRepository;
import com.actisys.paymentservice.service.impl.PaymentServiceImpl;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceUnitTests {

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private PaymentMapper paymentMapper;

  @Mock
  private RandomNumberClient randomNumberClient;

  @InjectMocks
  private PaymentServiceImpl paymentService;

  private PaymentCreateDTO paymentCreateDTO;
  private Payment payment;
  private PaymentDTO paymentDTO;

  @BeforeEach
  void setUp() {
    paymentCreateDTO = new PaymentCreateDTO(1L, 100L, BigDecimal.valueOf(500.00));

    payment = new Payment();
    payment.setId("payment-123");
    payment.setOrderId(1L);
    payment.setUserId(100L);
    payment.setPaymentAmount(BigDecimal.valueOf(500.00));
    payment.setStatus(PaymentStatus.SUCCESS);
    payment.setTimestamp(Instant.now());

    paymentDTO = new PaymentDTO("payment-123", 1L, 100L, PaymentStatus.SUCCESS, Instant.now(),
        BigDecimal.valueOf(500.00));
  }

  @Test
  @DisplayName("Should create payment with SUCCESS status when random number is even")
  void testCreatePaymentSuccessWhenEvenNumber() {
    when(randomNumberClient.getRandomNumber()).thenReturn(4);
    when(paymentMapper.toEntity(paymentCreateDTO)).thenReturn(payment);
    when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
    when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);

    PaymentDTO result = paymentService.createPayment(paymentCreateDTO);

    assertNotNull(result);
    assertEquals(PaymentStatus.SUCCESS, result.getStatus());
    verify(randomNumberClient, times(1)).getRandomNumber();
  }

  @Test
  @DisplayName("Should create payment with FAILED status when random number is odd")
  void testCreatePaymentFailedWhenOddNumber() {
    PaymentDTO failedPaymentDTO = new PaymentDTO("payment-456", 1L, 100L, PaymentStatus.FAILED,
        Instant.now(), BigDecimal.valueOf(500.00));

    when(randomNumberClient.getRandomNumber()).thenReturn(7);
    when(paymentMapper.toEntity(paymentCreateDTO)).thenReturn(payment);
    when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
    when(paymentMapper.toDto(payment)).thenReturn(failedPaymentDTO);

    PaymentDTO result = paymentService.createPayment(paymentCreateDTO);

    assertEquals(PaymentStatus.FAILED, result.getStatus());
  }

  @Test
  @DisplayName("Should delete payment successfully when it exists")
  void testDeletePaymentSuccess() {
    String paymentId = "payment-123";
    when(paymentRepository.existsById(paymentId)).thenReturn(true);

    paymentService.deletePayment(paymentId);

    verify(paymentRepository, times(1)).existsById(paymentId);
    verify(paymentRepository, times(1)).deleteById(paymentId);
  }

  @Test
  @DisplayName("Should throw exception when deleting non-existing payment")
  void testDeletePaymentThrowsException() {
    String paymentId = "non-existing-id";
    when(paymentRepository.existsById(paymentId)).thenReturn(false);

    PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
        () -> paymentService.deletePayment(paymentId));

    assertTrue(exception.getMessage().contains(paymentId));
    verify(paymentRepository, never()).deleteById(anyString());
  }


  @Test
  @DisplayName("Should verify existence before deletion")
  void testDeletePaymentVerifiesExistenceFirst() {
    String paymentId = "payment-456";
    when(paymentRepository.existsById(paymentId)).thenReturn(true);

    paymentService.deletePayment(paymentId);

    InOrder inOrder = inOrder(paymentRepository);
    inOrder.verify(paymentRepository).existsById(paymentId);
    inOrder.verify(paymentRepository).deleteById(paymentId);
  }

  @Test
  @DisplayName("Should find payment by id successfully")
  void testFindPaymentByIdSuccess() {
    String paymentId = "payment-123";
    when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
    when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);

    PaymentDTO result = paymentService.getPaymentById(paymentId);

    assertNotNull(result);
    assertEquals("payment-123", result.getId());
    assertEquals(1L, result.getOrderId());
  }

  @Test
  @DisplayName("Should throw exception when payment not found by id")
  void testFindPaymentByIdNotFound() {
    String paymentId = "non-existing-id";
    when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

    PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
        () -> paymentService.getPaymentById(paymentId));

    assertTrue(exception.getMessage().contains(paymentId));
  }

  @Test
  @DisplayName("Should find all payments by order id")
  void testFindPaymentsByOrderIdSuccess() {
    Long orderId = 1L;
    Payment payment2 = new Payment();
    payment2.setId("payment-456");
    payment2.setOrderId(orderId);
    payment2.setUserId(100L);
    payment2.setPaymentAmount(BigDecimal.valueOf(300.00));
    payment2.setStatus(PaymentStatus.SUCCESS);
    payment2.setTimestamp(Instant.now());

    PaymentDTO paymentDTO2 = new PaymentDTO("payment-456", orderId, 100L, PaymentStatus.SUCCESS,
        Instant.now(), BigDecimal.valueOf(300.00));

    when(paymentRepository.findByOrderId(orderId)).thenReturn(Arrays.asList(payment, payment2));
    when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);
    when(paymentMapper.toDto(payment2)).thenReturn(paymentDTO2);

    List<PaymentDTO> result = paymentService.getPaymentsByOrderId(orderId);

    assertEquals(2, result.size());
    assertEquals("payment-123", result.get(0).getId());
  }

  @Test
  @DisplayName("Should return empty list when no payments found for order")
  void testFindPaymentsByOrderIdEmpty() {
    Long orderId = 999L;
    when(paymentRepository.findByOrderId(orderId)).thenReturn(Collections.emptyList());

    List<PaymentDTO> result = paymentService.getPaymentsByOrderId(orderId);

    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Should find all payments by user id")
  void testFindPaymentsByUserIdSuccess() {
    Long userId = 100L;
    when(paymentRepository.findByUserId(userId)).thenReturn(Arrays.asList(payment));
    when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);

    List<PaymentDTO> result = paymentService.getPaymentsByUserId(userId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(userId, result.get(0).getUserId());
  }

  @Test
  @DisplayName("Should find payments by status")
  void testFindPaymentsByStatusSuccess() {
    List<PaymentStatus> statuses = Arrays.asList(PaymentStatus.SUCCESS);
    when(paymentRepository.findByStatusIn(statuses)).thenReturn(Arrays.asList(payment));
    when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);

    List<PaymentDTO> result = paymentService.findPaymentsByStatus(statuses);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(PaymentStatus.SUCCESS, result.get(0).getStatus());
  }

  @Test
  @DisplayName("Should calculate total sum for period successfully")
  void testGetTotalSumForPeriodSuccess() {
    Instant from = Instant.parse("2025-11-01T00:00:00Z");
    Instant to = Instant.parse("2025-11-02T23:59:59Z");
    Double expectedSum = 1500.00;

    when(paymentRepository.getTotalSumForPeriod(from, to)).thenReturn(expectedSum);

    Double result = paymentService.getTotalSumForPeriod(from, to);

    assertNotNull(result);
    assertEquals(expectedSum, result);
  }

  @Test
  @DisplayName("Should return zero when no payments in period")
  void testGetTotalSumForPeriodZero() {
    Instant from = Instant.parse("2025-11-01T00:00:00Z");
    Instant to = Instant.parse("2025-11-02T23:59:59Z");

    when(paymentRepository.getTotalSumForPeriod(from, to)).thenReturn(0.0);

    Double result = paymentService.getTotalSumForPeriod(from, to);

    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("Should handle null result from repository")
  void testGetTotalSumForPeriodNull() {
    Instant from = Instant.parse("2025-11-01T00:00:00Z");
    Instant to = Instant.parse("2025-11-02T23:59:59Z");

    when(paymentRepository.getTotalSumForPeriod(from, to)).thenReturn(null);

    Double result = paymentService.getTotalSumForPeriod(from, to);

    assertNull(result);
  }

  @Test
  @DisplayName("Should handle large payment amounts")
  void testCreatePaymentWithLargeAmount() {
    PaymentCreateDTO largeAmountDTO = new PaymentCreateDTO(1L, 100L,
        new BigDecimal("999999.99"));

    Payment largePayment = new Payment();
    largePayment.setId("payment-large");
    largePayment.setOrderId(1L);
    largePayment.setUserId(100L);
    largePayment.setPaymentAmount(new BigDecimal("999999.99"));
    largePayment.setStatus(PaymentStatus.SUCCESS);
    largePayment.setTimestamp(Instant.now());

    PaymentDTO largePaymentDTO = new PaymentDTO("payment-large", 1L, 100L, PaymentStatus.SUCCESS,
        Instant.now(), new BigDecimal("999999.99"));

    when(randomNumberClient.getRandomNumber()).thenReturn(10);
    when(paymentMapper.toEntity(largeAmountDTO)).thenReturn(largePayment);
    when(paymentRepository.save(any(Payment.class))).thenReturn(largePayment);
    when(paymentMapper.toDto(largePayment)).thenReturn(largePaymentDTO);

    PaymentDTO result = paymentService.createPayment(largeAmountDTO);

    assertNotNull(result);
    assertEquals(new BigDecimal("999999.99"), result.getPaymentAmount());
  }
}
