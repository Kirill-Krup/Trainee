package com.actisys.paymentservice.exception;

public class PaymentNotFoundException extends RuntimeException {

  public PaymentNotFoundException(String id) {
    super("Cannot find payment with id " + id);
  }
}
