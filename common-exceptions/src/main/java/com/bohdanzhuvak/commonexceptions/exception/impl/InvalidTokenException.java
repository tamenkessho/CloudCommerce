package com.bohdanzhuvak.commonexceptions.exception.impl;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;

public class InvalidTokenException extends BaseException {
  public InvalidTokenException() {
    super("Invalid or expired token", "INVALID_TOKEN");
  }
}