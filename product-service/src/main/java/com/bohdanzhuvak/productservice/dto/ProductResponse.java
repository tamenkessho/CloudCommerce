package com.bohdanzhuvak.productservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse (
        String id,
        String name,
        String description,
        String category,
        BigDecimal price
){}
