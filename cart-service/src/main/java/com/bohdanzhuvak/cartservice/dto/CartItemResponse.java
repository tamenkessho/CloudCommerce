package com.bohdanzhuvak.cartservice.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CartItemResponse(
    String productId,
    BigDecimal pricePerUnit,
    String quantity) {

}
