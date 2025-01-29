package com.bohdanzhuvak.orderservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class Order {
  @Id
  private String id;
  private String userId;
  private OrderStatus status;
  private BigDecimal totalPrice;
  private List<OrderItem> items;
  private Instant createdAt;
}

