package com.example.demo.Exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("User " + id + " not found");
  }
}
