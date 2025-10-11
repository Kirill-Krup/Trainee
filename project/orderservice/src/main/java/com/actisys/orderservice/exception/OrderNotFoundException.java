package com.actisys.orderservice.exception;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(Long id) {
    super("Cannot find order with id " + id);
  }
}
