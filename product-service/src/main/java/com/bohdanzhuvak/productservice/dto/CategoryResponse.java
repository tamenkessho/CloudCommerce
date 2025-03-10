package com.bohdanzhuvak.productservice.dto;

import lombok.Builder;

@Builder
public record CategoryResponse(
    String id,
    String name
) {

}
