package com.bohdanzhuvak.orderservice.service;

import com.bohdanzhuvak.commonexceptions.exception.impl.InvalidOrderStateException;
import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.commonsecurity.utils.SecurityUtils;
import com.bohdanzhuvak.orderservice.client.ProductClient;
import com.bohdanzhuvak.orderservice.dto.OrderRequest;
import com.bohdanzhuvak.orderservice.dto.OrderResponse;
import com.bohdanzhuvak.orderservice.dto.ProductResponse;
import com.bohdanzhuvak.orderservice.mapper.OrderMapper;
import com.bohdanzhuvak.orderservice.model.Order;
import com.bohdanzhuvak.orderservice.model.OrderItem;
import com.bohdanzhuvak.orderservice.model.OrderStatus;
import com.bohdanzhuvak.orderservice.repository.OrderRepository;
import feign.FeignException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
  private final OrderRepository orderRepository;
  private final ProductClient productClient;
  private final SecurityUtils securityUtils;
  private final OrderMapper orderMapper;

  @Transactional
  @PreAuthorize("hasRole('USER')")
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
                throw new ResourceNotFoundException("Product with id " + item.productId() + " not found");
              }
            }).toList();

    BigDecimal totalPrice = orderItems.stream()
            .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String userId = securityUtils.getCurrentUserId();

    Order order = Order.builder()
            .userId(userId)
            .status(OrderStatus.CREATED)
            .totalPrice(totalPrice)
            .items(orderItems)
            .build();

    order = orderRepository.save(order);
    log.info("Created order with id: {}", order.getId());
    return orderMapper.toResponse(order);
  }

  @PreAuthorize("hasRole('USER')")
  public OrderResponse getOrderById(String id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + id + " not found"));

    securityUtils.checkAccessToUserData(order.getUserId());

    log.info("Successfully retrieved order: {}", order.getId());
    return orderMapper.toResponse(order);
  }

  @PreAuthorize("#userId == authentication.name or hasRole('ADMIN')")
  public List<OrderResponse> getOrdersByUserId(String userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    log.info("Retrieved {} orders", orders.size());
    return orders.stream()
            .map(orderMapper::toResponse)
            .toList();
  }

  @Transactional
  @PreAuthorize("hasRole('USER')")
  public OrderResponse cancelOrder(String id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + id + " not found"));

    securityUtils.checkAccessToUserData(order.getUserId());

    if (order.getStatus() != OrderStatus.CREATED) {
      throw new InvalidOrderStateException("Cannot cancel order in status: " + order.getStatus());
    }

    order.setStatus(OrderStatus.CANCELLED);
    order = orderRepository.save(order);
    log.info("Canceled order with id: {}", order.getId());
    return orderMapper.toResponse(order);
  }
}
