package com.bohdanzhuvak.orderservice;

import com.bohdanzhuvak.orderservice.client.ProductClient;
import com.bohdanzhuvak.orderservice.dto.OrderItemRequest;
import com.bohdanzhuvak.orderservice.dto.OrderRequest;
import com.bohdanzhuvak.orderservice.dto.OrderResponse;
import com.bohdanzhuvak.orderservice.dto.ProductResponse;
import com.bohdanzhuvak.orderservice.exception.OrderNotFoundException;
import com.bohdanzhuvak.orderservice.exception.ProductNotFoundException;
import com.bohdanzhuvak.orderservice.model.Order;
import com.bohdanzhuvak.orderservice.model.OrderItem;
import com.bohdanzhuvak.orderservice.model.OrderStatus;
import com.bohdanzhuvak.orderservice.repository.OrderRepository;
import com.bohdanzhuvak.orderservice.service.OrderService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock private ProductClient productClient;
  @InjectMocks
  private OrderService orderService;

  @Test
  void createOrder_ShouldSaveOrderWithCorrectData() {
    // Arrange
    OrderItemRequest itemRequest = new OrderItemRequest("prod1", 2);
    OrderRequest request = new OrderRequest("user1", List.of(itemRequest));

    ProductResponse productResponse = new ProductResponse(
            "prod1", "Product 1", new BigDecimal("100.00")
    );

    when(productClient.getProductById("prod1")).thenReturn(productResponse);
    when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    OrderResponse response = orderService.createOrder(request);

    // Assert
    assertThat(response.userId()).isEqualTo("user1");
    assertThat(response.totalPrice()).isEqualByComparingTo("200.00");
    assertThat(response.items()).hasSize(1);

    verify(orderRepository).save(any(Order.class));
  }

  @Test
  void createOrder_ShouldThrowWhenProductNotFound() {
    // Arrange
    OrderItemRequest itemRequest = new OrderItemRequest("invalid", 1);
    OrderRequest request = new OrderRequest("user1", List.of(itemRequest));

    when(productClient.getProductById("invalid"))
            .thenThrow(new FeignException.NotFound("", Request.create(
                    Request.HttpMethod.GET,
                    "/api/products/123",
                    Collections.emptyMap(),
                    null,
                    Charset.defaultCharset(),
                    null
            ), null, null));

    // Act & Assert
    assertThrows(ProductNotFoundException.class,
            () -> orderService.createOrder(request));

    verify(orderRepository, never()).save(any());
  }

  @Test
  void createOrder_ShouldCalculateTotalPriceCorrectly() {
    // Arrange
    List<OrderItemRequest> items = List.of(
            new OrderItemRequest("prod1", 2),
            new OrderItemRequest("prod2", 1)
    );

    when(productClient.getProductById("prod1"))
            .thenReturn(new ProductResponse("prod1", "P1", new BigDecimal("50.00")));
    when(productClient.getProductById("prod2"))
            .thenReturn(new ProductResponse("prod2", "P2", new BigDecimal("100.00")));
    when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    // Act
    OrderResponse response = orderService.createOrder(
            new OrderRequest("user1", items)
    );

    // Assert
    assertThat(response.totalPrice()).isEqualByComparingTo("200.00");
  }


  @Test
  void getOrderById_ShouldReturnOrderWhenExists() {
    // Arrange
    Order order = createTestOrder();
    when(orderRepository.findById("order123")).thenReturn(Optional.of(order));

    // Act
    OrderResponse response = orderService.getOrderById("order123");

    // Assert
    assertThat(response.id()).isEqualTo(order.getId());
  }

  @Test
  void getOrderById_ShouldThrowWhenOrderNotFound() {
    // Arrange
    when(orderRepository.findById("invalid")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(OrderNotFoundException.class,
            () -> orderService.getOrderById("invalid"));
  }

  @Test
  void getOrdersByUserId_ShouldReturnAllUserOrders() {
    // Arrange
    Order order = createTestOrder();
    order.setId("order456");
    List<Order> orders = List.of(
            createTestOrder(),
            order
    );

    when(orderRepository.findByUserId("user1")).thenReturn(orders);

    // Act
    List<OrderResponse> result = orderService.getOrdersByUserId("user1");

    // Assert
    assertThat(result).hasSize(2);
  }

  @Test
  void getOrdersByUserId_ShouldReturnEmptyListWhenNoOrders() {
    // Arrange
    when(orderRepository.findByUserId("user2")).thenReturn(Collections.emptyList());

    // Act
    List<OrderResponse> result = orderService.getOrdersByUserId("user2");

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  void cancelOrder_ShouldUpdateStatusToCancelled() {
    // Arrange
    Order order = createTestOrder();
    order.setStatus(OrderStatus.CREATED);
    when(orderRepository.findById("order123")).thenReturn(Optional.of(order));
    when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    // Act
    OrderResponse response = orderService.cancelOrder("order123");

    // Assert
    assertThat(response.status()).isEqualTo(OrderStatus.CANCELLED);
    verify(orderRepository).save(order);
  }

  @Test
  void cancelOrder_ShouldThrowWhenOrderNotInCreatedState() {
    // Arrange
    Order order = createTestOrder();
    order.setStatus(OrderStatus.CANCELLED);
    when(orderRepository.findById("order123")).thenReturn(Optional.of(order));

    // Act & Assert
    assertThrows(IllegalStateException.class,
            () -> orderService.cancelOrder("order123"));

    verify(orderRepository, never()).save(any());
  }

  @Test
  void cancelOrder_ShouldThrowWhenOrderNotFound() {
    // Arrange
    when(orderRepository.findById("invalid")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(OrderNotFoundException.class,
            () -> orderService.cancelOrder("invalid"));
  }

  private Order createTestOrder() {
    return Order.builder()
            .id("order123")
            .userId("user1")
            .status(OrderStatus.CREATED)
            .totalPrice(new BigDecimal("100.00"))
            .items(List.of(
                    OrderItem.builder()
                            .productId("prod1")
                            .productName("Product 1")
                            .quantity(1)
                            .pricePerUnit(new BigDecimal("100.00"))
                            .build()
            ))
            .createdAt(Instant.now())
            .build();
  }
}