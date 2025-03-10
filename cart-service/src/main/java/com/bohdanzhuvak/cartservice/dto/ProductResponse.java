package com.bohdanzhuvak.cartservice.dto;

import java.math.BigDecimal;

public record ProductResponse(
    String id,
    String name,
    BigDecimal price
) {

}