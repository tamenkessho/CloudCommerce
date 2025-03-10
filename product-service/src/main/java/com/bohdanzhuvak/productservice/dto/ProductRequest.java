package com.bohdanzhuvak.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductRequest(
    @NotBlank
    String name,
    String categoryId,
    String description,
    @DecimalMin("0.01")
    BigDecimal price
) {

}
