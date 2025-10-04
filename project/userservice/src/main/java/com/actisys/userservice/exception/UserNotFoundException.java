package com.actisys.userservice.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("User " + id + " not found");
  }

  public UserNotFoundException(String email) {
    super("User " + email + " not found");
  }

}
