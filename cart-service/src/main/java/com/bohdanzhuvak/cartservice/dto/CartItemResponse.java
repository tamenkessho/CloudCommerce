package com.bohdanzhuvak.cartservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse (
    String productId,
    BigDecimal pricePerUnit,
    String quantity) {}
