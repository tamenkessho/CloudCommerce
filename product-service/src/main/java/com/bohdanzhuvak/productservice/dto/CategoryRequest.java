package com.bohdanzhuvak.productservice.dto;

import lombok.Builder;

@Builder
public record CategoryRequest (
        String name
){}
