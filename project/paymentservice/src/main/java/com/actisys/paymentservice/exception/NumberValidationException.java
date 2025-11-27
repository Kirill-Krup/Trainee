package com.actisys.paymentservice.exception;

public class NumberValidationException extends RuntimeException {

  public NumberValidationException() {
    super("Error occurred in validation number from random api");
  }
}
