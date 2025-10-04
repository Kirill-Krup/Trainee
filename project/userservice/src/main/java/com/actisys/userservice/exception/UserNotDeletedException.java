package com.actisys.userservice.exception;

public class UserNotDeletedException extends RuntimeException {
  public UserNotDeletedException(Long id) {
    super("User with id " + id + " was not deleted because it does not exist in database");
  }

  public UserNotDeletedException(String message) {
    super(message);
  }
}