package com.bohdanzhuvak.cartservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DOUBLE;

@Data
@Builder
public class CartItem {
  private String productId;
  @Field(targetType = DOUBLE)
  private BigDecimal pricePerUnit;
  private int quantity;
}
