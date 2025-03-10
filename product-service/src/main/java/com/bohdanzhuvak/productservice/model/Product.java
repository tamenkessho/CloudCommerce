package com.bohdanzhuvak.productservice.model;

import static org.springframework.data.mongodb.core.mapping.FieldType.DOUBLE;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder(toBuilder = true)
public class Product {

  @Id
  private String id;
  private String name;
  private Category category;
  private String description;
  @Field(targetType = DOUBLE)
  private BigDecimal price;
  private double discount;
  private List<String> images;
  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
