package com.bohdanzhuvak.productservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Product {
  @Id
  private String id;
  private String name;
  private String categoryId;
  private String description;
  private double price;
  private double discount;
  private List<String> images;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
