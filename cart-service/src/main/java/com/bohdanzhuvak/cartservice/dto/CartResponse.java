package com.bohdanzhuvak.cartservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CartResponse (
    String id,
    String userId,
    List<CartItemResponse> items
){ }
