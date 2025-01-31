package com.bohdanzhuvak.userservice.exception;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(String message) {
    super(String.format("Invalid token: %s", message));
  }
}
