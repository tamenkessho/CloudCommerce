package com.bohdanzhuvak.commonexceptions.exception.impl;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;

public class UserAlreadyExistsException extends BaseException {

  public UserAlreadyExistsException(String email) {
    super("User with email " + email + " already exists", "USER_ALREADY_EXISTS");
  }
}

