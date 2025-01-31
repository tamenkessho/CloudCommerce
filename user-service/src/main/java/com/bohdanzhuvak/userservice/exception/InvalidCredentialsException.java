package com.bohdanzhuvak.userservice.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Wrong credentials");
  }
}
