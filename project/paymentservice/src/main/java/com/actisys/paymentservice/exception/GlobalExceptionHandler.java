package com.actisys.paymentservice.exception;

import com.actisys.paymentservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PaymentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException e) {
    ErrorResponse error = new ErrorResponse("PAYMENT_NOT_FOUND", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(RandomApiException.class)
  public ResponseEntity<ErrorResponse> handleRandomApiException(RandomApiException e) {
    ErrorResponse error = new ErrorResponse("RANDOM_API_EXCEPTION", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(NumberValidationException.class)
  public ResponseEntity<ErrorResponse> handleNumberValidationException(NumberValidationException e) {
    ErrorResponse error = new ErrorResponse("NUMBER_VALIDATION_EXCEPTION", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
}

