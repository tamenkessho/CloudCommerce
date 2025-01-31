package com.bohdanzhuvak.orderservice.service;

import com.bohdanzhuvak.orderservice.client.ProductClient;
import com.bohdanzhuvak.orderservice.dto.OrderItemResponse;
import com.bohdanzhuvak.orderservice.dto.OrderRequest;
import com.bohdanzhuvak.orderservice.dto.OrderResponse;
import com.bohdanzhuvak.orderservice.dto.ProductResponse;
import com.bohdanzhuvak.orderservice.exception.OrderNotFoundException;
import com.bohdanzhuvak.orderservice.exception.ProductNotFoundException;
import com.bohdanzhuvak.orderservice.model.Order;
import com.bohdanzhuvak.orderservice.model.OrderItem;
import com.bohdanzhuvak.orderservice.model.OrderStatus;
import com.bohdanzhuvak.orderservice.repository.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
  private final OrderRepository orderRepository;
  private final ProductClient productClient;

  @Transactional
  public OrderResponse createOrder(OrderRequest request) {
    List<OrderItem> orderItems = request.items().stream()
            .map(item -> {
              try {
                ProductResponse product = productClient.getProductById(item.productId());
                return OrderItem.builder()
                        .productId(product.id())
                        .productName(product.name())
                        .pricePerUnit(product.price())
                        .quantity(item.quantity())
                        .build();
              } catch (FeignException.NotFound ex) {
                throw new ProductNotFoundException("Product not found: " + item.productId());
              }
            }).toList();

    BigDecimal totalPrice = orderItems.stream()
            .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    Order order = Order.builder()
            .userId(request.userId())
            .status(OrderStatus.CREATED)
            .totalPrice(totalPrice)
            .items(orderItems)
            .createdAt(Instant.now())
            .build();

    order = orderRepository.save(order);
    log.info("Created order with id: {}", order.getId());
    return mapToResponse(order);
  }

  public OrderResponse getOrderById(String id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    log.info("Successfully retrieved order: {}", order.getId());
    return mapToResponse(order);
  }

  public List<OrderResponse> getOrdersByUserId(String userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    log.info("Retrieved {} orders", orders.size());
    return orders.stream()
            .map(this::mapToResponse)
            .toList();
  }

  @Transactional
  public OrderResponse cancelOrder(String id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

    if (order.getStatus() != OrderStatus.CREATED) {
      throw new IllegalStateException("Cannot cancel order in status: " + order.getStatus());
    }

    order.setStatus(OrderStatus.CANCELLED);
    order = orderRepository.save(order);
    log.info("Canceled order with id: {}", order.getId());
    return mapToResponse(order);
  }


  private OrderResponse mapToResponse(Order order) {
    return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getTotalPrice(),
            order.getItems().stream()
                    .map(item -> new OrderItemResponse(
                            item.getProductId(),
                            item.getProductName(),
                            item.getQuantity(),
                            item.getPricePerUnit(),
                            item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity()))
                    ))
                    .toList()
    );
  }
}
