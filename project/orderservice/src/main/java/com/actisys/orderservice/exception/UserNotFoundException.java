package com.actisys.orderservice.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String email) {
    super("User " + email + " not found");
  }

}
