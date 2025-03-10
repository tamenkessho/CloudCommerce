package com.bohdanzhuvak.commonexceptions.exception.impl;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;

public class ServiceUnavailableException extends BaseException {

  public ServiceUnavailableException(String serviceName) {
    super("Service " + serviceName + " is unavailable", "SERVICE_UNAVAILABLE");
  }
}
