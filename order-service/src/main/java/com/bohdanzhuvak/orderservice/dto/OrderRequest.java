package com.bohdanzhuvak.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequest(
    @NotEmpty(message = "Order items cannot be empty")
    List<OrderItemRequest> items
) {

}
