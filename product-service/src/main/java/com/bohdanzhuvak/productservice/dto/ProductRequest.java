package com.bohdanzhuvak.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest (
        @NotBlank
        String name,
        String categoryId,
        String description,
        @DecimalMin("0.01")
        BigDecimal price
){}
