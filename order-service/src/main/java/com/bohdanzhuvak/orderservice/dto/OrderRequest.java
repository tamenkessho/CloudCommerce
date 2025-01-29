package com.bohdanzhuvak.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
        @NotBlank(message = "User ID is required")
        String userId,
        @NotEmpty(message = "Order items cannot be empty")
        List<OrderItemRequest> items
) {}
