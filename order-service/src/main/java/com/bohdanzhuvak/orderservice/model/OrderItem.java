package com.bohdanzhuvak.orderservice.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {

  private String productId;
  private String productName;
  private int quantity;
  private BigDecimal pricePerUnit;
}
