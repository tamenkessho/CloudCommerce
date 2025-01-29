package com.bohdanzhuvak.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        String productName,
        int quantity,
        BigDecimal pricePerUnit,
        BigDecimal totalPrice
) {}
