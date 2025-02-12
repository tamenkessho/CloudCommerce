package com.bohdanzhuvak.commonexceptions.model;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String message,
        List<String> errors,
        Instant timestamp
) {
  public ErrorResponse(String message) {
    this(message, null, Instant.now());
  }

  public ErrorResponse(String message, List<String> errors) {
    this(message, errors, Instant.now());
  }
}
