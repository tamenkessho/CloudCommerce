package com.bohdanzhuvak.productservice.dto;

import lombok.Data;

@Data
public class ProductCreateDTO {
  private String name;
  private String categoryId;
  private String description;
  private double price;
}
