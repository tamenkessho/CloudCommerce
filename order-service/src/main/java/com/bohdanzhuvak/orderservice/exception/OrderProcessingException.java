package com.bohdanzhuvak.orderservice.exception;

public class OrderProcessingException extends RuntimeException {
  public OrderProcessingException(String message) {
    super(message);
  }
}
