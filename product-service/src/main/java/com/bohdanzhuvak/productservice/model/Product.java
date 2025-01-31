package com.bohdanzhuvak.productservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Product {
  @Id
  private String id;
  private String name;
  private Category category;
  private String description;
  private BigDecimal price;
  private double discount;
  private List<String> images;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
