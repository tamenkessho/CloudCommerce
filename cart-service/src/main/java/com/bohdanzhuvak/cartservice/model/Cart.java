package com.bohdanzhuvak.cartservice.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder(toBuilder = true)
public class Cart {

  @Id
  private String id;
  private String userId;
  private List<CartItem> items;
  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
