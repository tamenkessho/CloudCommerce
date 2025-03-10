package com.bohdanzhuvak.cartservice.mapper;

import com.bohdanzhuvak.cartservice.dto.CartItemRequest;
import com.bohdanzhuvak.cartservice.dto.CartResponse;
import com.bohdanzhuvak.cartservice.model.Cart;
import com.bohdanzhuvak.cartservice.model.CartItem;
import java.math.BigDecimal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

  Cart toCart(CartResponse cart);

  CartResponse toCartResponse(Cart cart);

  CartItem toCartItem(CartItemRequest cartItemRequest, BigDecimal pricePerUnit);
}
