package com.bohdanzhuvak.productservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
public class Category {
  @Id
  private String id;
  private String name;
}
