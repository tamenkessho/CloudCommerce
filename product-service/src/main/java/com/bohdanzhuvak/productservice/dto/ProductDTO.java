package com.bohdanzhuvak.productservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDTO {
  private String id;
  private String name;
  private String description;
  private String category;
  private double price;
}
