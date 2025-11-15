package com.actisys.paymentservice.service.impl;

import com.actisys.paymentservice.client.RandomNumberClient;
import com.actisys.paymentservice.dto.PaymentCreateDTO;
import com.actisys.paymentservice.dto.PaymentDTO;
import com.actisys.paymentservice.exception.PaymentNotFoundException;
import com.actisys.paymentservice.mapper.PaymentMapper;
import com.actisys.paymentservice.model.Payment;
import com.actisys.paymentservice.model.enums.PaymentStatus;
import com.actisys.paymentservice.repository.PaymentRepository;
import com.actisys.paymentservice.service.PaymentService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;
  private final RandomNumberClient randomNumberClient;

  @Override
  public PaymentDTO createPayment(PaymentCreateDTO paymentDTO) {
    int randomNumber = randomNumberClient.getRandomNumber();
    log.debug("Api sent randomNumber: {}", randomNumber);
    PaymentStatus status = (randomNumber%2 == 0) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    Payment payment = paymentMapper.toEntity(paymentDTO);
    payment.setStatus(status);
    payment.setTimestamp(Instant.now());
    return paymentMapper.toDto(paymentRepository.save(payment));
  }

  @Override
  public void deletePayment(String id) {
    if(!paymentRepository.existsById(id)) {
      throw new PaymentNotFoundException(id);
    }
    paymentRepository.deleteById(id);
  }

  @Override
  public PaymentDTO getPaymentById(String id) {
    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new PaymentNotFoundException(id));
    return paymentMapper.toDto(payment);
  }

  @Override
  public List<PaymentDTO> getPaymentsByOrderId(Long orderId) {
    return paymentRepository.findByOrderId(orderId)
        .stream()
        .map(paymentMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<PaymentDTO> getPaymentsByUserId(Long userId) {
    return paymentRepository.findByUserId(userId)
        .stream()
        .map(paymentMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<PaymentDTO> findPaymentsByStatus(List<PaymentStatus> statuses) {
    return paymentRepository.findByStatusIn(statuses)
        .stream()
        .map(paymentMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public Double getTotalSumForPeriod(Instant from, Instant to) {
    return paymentRepository.getTotalSumForPeriod(from, to);
  }

}
