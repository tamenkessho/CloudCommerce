package com.bohdanzhuvak.cartservice.service;

import com.bohdanzhuvak.cartservice.client.ProductClient;
import com.bohdanzhuvak.cartservice.dto.CartItemRequest;
import com.bohdanzhuvak.cartservice.dto.CartResponse;
import com.bohdanzhuvak.cartservice.dto.ProductResponse;
import com.bohdanzhuvak.cartservice.mapper.CartMapper;
import com.bohdanzhuvak.cartservice.model.Cart;
import com.bohdanzhuvak.cartservice.model.CartItem;
import com.bohdanzhuvak.cartservice.repository.CartRepository;
import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
  private final CartMapper cartMapper;
  private final ProductClient productClient;

  @Transactional
  public CartResponse addItem(CartItemRequest cartItemRequest, String userId) {
    ProductResponse product = getProductOrThrow(cartItemRequest.productId());
    CartItem cartItem = cartMapper.toCartItem(cartItemRequest, product.price());

    Cart cart = cartRepository.findByUserId(userId)
        .map(existingCart -> addItemToCart(existingCart, cartItem))
        .orElseGet(() -> createNewCart(userId, cartItem));

    return cartMapper.toCartResponse(cartRepository.save(cart));
  }

  public CartResponse removeItemById(String productId, String userId) {
    Cart cartSaved = cartRepository.findByUserId(userId)
        .map(cart -> removeItemFromCart(cart, productId))
        .map(cartRepository::save)
        .orElseThrow(() -> new ResourceNotFoundException("Cart of user " + userId + " not found"));
    return cartMapper.toCartResponse(cartSaved);
  }

  public CartResponse getByUserId(String userId) {
    return cartRepository.findByUserId(userId)
        .map(cartMapper::toCartResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Cart of user " + userId + " not found"));
  }

  public CartResponse getById(String cartId) {
    return cartRepository.findById(cartId)
        .map(cartMapper::toCartResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));
  }

  public void deleteByUserId(String userId) {
    cartRepository.findByUserId(userId)
        .ifPresentOrElse(
            cartRepository::delete,
            () -> {throw new ResourceNotFoundException("Cart of user " + userId + " not found");}
        );
  }

  private ProductResponse getProductOrThrow(String productId) {
    try {
      return productClient.getProductById(productId);
    } catch (FeignException.NotFound ex) {
      throw new ResourceNotFoundException("Product with id " + productId + " not found");
    }
  }


  private Cart addItemToCart(Cart existingCart, CartItem newItem) {
    List<CartItem> updatedItems = new ArrayList<>(existingCart.getItems());
    updatedItems.add(newItem);
    existingCart.setItems(updatedItems);
    return existingCart;
  }

  private Cart removeItemFromCart(Cart cart, String productId) {
    List<CartItem> filteredItems = cart.getItems().stream()
        .filter(item -> !item.getProductId().equals(productId))
        .toList();
    cart.setItems(filteredItems);
    return cart;
  }

  private Cart createNewCart(String userId, CartItem item) {
    return Cart.builder()
        .userId(userId)
        .items(new ArrayList<>(List.of(item)))
        .build();
  }
}
