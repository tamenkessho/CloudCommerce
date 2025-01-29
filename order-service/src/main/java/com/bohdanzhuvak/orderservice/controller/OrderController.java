package com.bohdanzhuvak.orderservice.controller;

import com.bohdanzhuvak.orderservice.dto.OrderRequest;
import com.bohdanzhuvak.orderservice.dto.OrderResponse;
import com.bohdanzhuvak.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    return orderService.createOrder(orderRequest);
  }

  @GetMapping("/{id}")
  public OrderResponse getOrderById(@PathVariable String id) {
    return orderService.getOrderById(id);
  }

  @GetMapping
  public List<OrderResponse> getOrdersByUserId(@RequestParam String userId) {
    return orderService.getOrdersByUserId(userId);
  }

  @PutMapping("/{id}/cancel")
  public OrderResponse cancelOrder(@PathVariable String id) {
    return orderService.cancelOrder(id);
  }

}
