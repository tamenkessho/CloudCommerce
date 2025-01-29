package com.bohdanzhuvak.orderservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItem {
  private String productId;
  private String productName;
  private int quantity;
  private BigDecimal pricePerUnit;
}
