package com.bohdanzhuvak.cartservice.controller;

import com.bohdanzhuvak.cartservice.dto.CartItemRequest;
import com.bohdanzhuvak.cartservice.dto.CartResponse;
import com.bohdanzhuvak.cartservice.service.CartService;
import com.bohdanzhuvak.commonsecurity.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;
  private final SecurityUtils securityUtils;

  @GetMapping
  public CartResponse getCartOfCurrentUser() {
    String userId = securityUtils.getCurrentUserId();
    return cartService.getByUserId(userId);
  }

  @GetMapping("/{id}")
  public CartResponse getCartById(@PathVariable String id) {
    return cartService.getById(id);
  }

  @DeleteMapping
  public void deleteCartOfCurrentUser() {
    String userId = securityUtils.getCurrentUserId();
    cartService.deleteByUserId(userId);
  }

  @PostMapping("/items")
  public CartResponse addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
    String userId = securityUtils.getCurrentUserId();
    return cartService.addItem(cartItemRequest, userId);
  }

  @DeleteMapping("/items")
  public CartResponse deleteItemFromCart(@RequestParam String productId) {
    String userId = securityUtils.getCurrentUserId();
    return cartService.removeItemById(productId, userId);
  }

}
