package com.bohdanzhuvak.orderservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder
public class Order {

  @Id
  private String id;
  private String userId;
  private OrderStatus status;
  private BigDecimal totalPrice;
  private List<OrderItem> items;
  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime updatedAt;
}

