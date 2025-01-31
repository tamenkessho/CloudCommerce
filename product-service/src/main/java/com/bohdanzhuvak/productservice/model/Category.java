package com.bohdanzhuvak.productservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class Category {
  @Id
  private String id;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
