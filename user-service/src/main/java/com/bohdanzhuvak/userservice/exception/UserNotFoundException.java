package com.bohdanzhuvak.userservice.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(@NotBlank @Email String email) {
    super(String.format("User with email %s not found", email));
  }
}
