package com.bohdanzhuvak.cartservice.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CartRequest(
    @NotEmpty(message = "Order items cannot be empty")
    List<CartItemRequest> items
) {}
