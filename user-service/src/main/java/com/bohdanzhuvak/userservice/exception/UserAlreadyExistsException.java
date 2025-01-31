package com.bohdanzhuvak.userservice.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(@NotBlank @Email String email) {
    super(String.format("User with email %s is already exist", email));
  }
}
