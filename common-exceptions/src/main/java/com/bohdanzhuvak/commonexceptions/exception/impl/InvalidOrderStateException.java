package com.bohdanzhuvak.commonexceptions.exception.impl;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;

public class InvalidOrderStateException extends BaseException {
  public InvalidOrderStateException(String message) {
    super(message, "INVALID_ORDER_STATE");
  }
}