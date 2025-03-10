package com.bohdanzhuvak.commonexceptions.exception.impl;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;

public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(String message) {
    super(message, "RESOURCE_NOT_FOUND");
  }
}