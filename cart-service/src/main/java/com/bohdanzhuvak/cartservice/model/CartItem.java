package com.bohdanzhuvak.cartservice.model;

import static org.springframework.data.mongodb.core.mapping.FieldType.DOUBLE;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class CartItem {

  private String productId;
  @Field(targetType = DOUBLE)
  private BigDecimal pricePerUnit;
  private int quantity;
}
