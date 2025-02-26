package com.bohdanzhuvak.commonexceptions.exception.impl;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;

public class InvalidCredentialsException extends BaseException {
  public InvalidCredentialsException() {
    super("Invalid credentials", "INVALID_CREDENTIALS");
  }
}