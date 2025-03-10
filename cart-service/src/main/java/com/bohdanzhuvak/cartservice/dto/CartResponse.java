package com.bohdanzhuvak.cartservice.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CartResponse(
    String id,
    String userId,
    List<CartItemResponse> items
) {

}
