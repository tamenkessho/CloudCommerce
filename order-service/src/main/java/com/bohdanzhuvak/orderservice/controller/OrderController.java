package com.bohdanzhuvak.orderservice.controller;

import com.bohdanzhuvak.orderservice.dto.OrderRequest;
import com.bohdanzhuvak.orderservice.dto.OrderResponse;
import com.bohdanzhuvak.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    log.info("Post /api/orders: {}", orderRequest);
    return orderService.createOrder(orderRequest);
  }

  @GetMapping("/{id}")
  public OrderResponse getOrderById(@PathVariable String id) {
    log.info("Get /api/orders/{}", id);
    return orderService.getOrderById(id);
  }

  @GetMapping
  public List<OrderResponse> getOrdersByUserId(@RequestParam String userId) {
    log.info("Get /api/orders?userId={}", userId);
    return orderService.getOrdersByUserId(userId);
  }

  @PutMapping("/{id}/cancel")
  public OrderResponse cancelOrder(@PathVariable String id) {
    log.info("Post /api/orders/{}/cancel", id);
    return orderService.cancelOrder(id);
  }

}
