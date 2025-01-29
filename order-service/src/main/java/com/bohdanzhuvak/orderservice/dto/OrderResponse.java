package com.bohdanzhuvak.orderservice.dto;

import com.bohdanzhuvak.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        String id,
        String userId,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemResponse> items
) {}