package com.bohdanzhuvak.orderservice;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.orderservice.controller.OrderController;
import com.bohdanzhuvak.orderservice.dto.OrderResponse;
import com.bohdanzhuvak.orderservice.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackages = {"com.bohdanzhuvak.commonexceptions",
    "com.bohdanzhuvak.commonsecurity"})
@WithMockUser("user1")
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private OrderService orderService;

  // Test for successful order creation
  @Test
  void createOrder_ShouldReturnCreatedOrder() throws Exception {
    String requestJson = """
        {
            "items": [
                {
                    "productId": "prod1",
                    "quantity": 2
                }
            ]
        }
        """;

    String expectedResponseJson = """
        {
            "id": "order123",
            "userId": "user1",
            "status": "CREATED",
            "totalPrice": 200.00,
            "items": [
                {
                    "productId": "prod1",
                    "productName": "Product 1",
                    "quantity": 2,
                    "pricePerUnit": 100.00,
                    "totalPrice": 200.00
                }
            ]
        }
        """;

    when(orderService.createOrder(any()))
        .thenReturn(objectMapper.readValue(expectedResponseJson, OrderResponse.class));

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json(expectedResponseJson));
  }

  // Test for an invalid request
  @Test
  void createOrder_ShouldReturn400ForInvalidRequest() throws Exception {
    String invalidRequestJson = """
        {
            "items": []
        }
        """;

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.path").value("/api/orders"))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.details[0].field").value("items"))
        .andExpect(jsonPath("$.details[0].message").value("Order items cannot be empty"));
  }

  // Test for retrieving an order by ID
  @Test
  void getOrderById_ShouldReturnOrder() throws Exception {
    String orderId = "order123";
    String expectedResponseJson = """
        {
            "id": "order123",
            "userId": "user1",
            "status": "CREATED",
            "totalPrice": 200.00,
            "items": [
                {
                    "productId": "prod1",
                    "productName": "Product 1",
                    "quantity": 2,
                    "pricePerUnit": 100.00,
                    "totalPrice": 200.00
                }
            ]
        }
        """;

    when(orderService.getOrderById(orderId))
        .thenReturn(objectMapper.readValue(expectedResponseJson, OrderResponse.class));

    mockMvc.perform(get("/api/orders/{id}", orderId))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponseJson));
  }

  // Test for retrieving a non-existent order
  @Test
  void getOrderById_ShouldReturn404WhenNotFound() throws Exception {
    String orderId = "non-existent";
    String expectedErrorJson = """
        {
            "message": "Order not found with id: non-existent"
        }
        """;

    when(orderService.getOrderById(orderId))
        .thenThrow(new ResourceNotFoundException("Order not found with id: non-existent"));

    mockMvc.perform(get("/api/orders/{id}", orderId))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expectedErrorJson));
  }

  // Test for retrieving a user's orders
  @Test
  void getOrdersByUserId_ShouldReturnOrdersList() throws Exception {
    String userId = "user1";
    String expectedResponseJson = """
        [
            {
                "id": "order123",
                "userId": "user1",
                "status": "CREATED",
                "totalPrice": 200.00,
                "items": [
                    {
                        "productId": "prod1",
                        "productName": "Product 1",
                        "quantity": 2,
                        "pricePerUnit": 100.00,
                        "totalPrice": 200.00
                    }
                ]
            },
            {
                "id": "order456",
                "userId": "user1",
                "status": "CREATED",
                "totalPrice": 300.00,
                "items": [
                    {
                        "productId": "prod2",
                        "productName": "Product 2",
                        "quantity": 3,
                        "pricePerUnit": 100.00,
                        "totalPrice": 300.00
                    }
                ]
            }
        ]
        """;

    when(orderService.getOrdersByUserId(userId))
        .thenReturn(objectMapper.readValue(expectedResponseJson, new TypeReference<>() {
        }));

    mockMvc.perform(get("/api/orders")
            .param("userId", userId))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponseJson));
  }

  // Test for order cancellation
  @Test
  void cancelOrder_ShouldUpdateOrderStatus() throws Exception {
    String orderId = "order123";
    String expectedResponseJson = """
        {
            "id": "order123",
            "userId": "user1",
            "status": "CANCELLED",
            "totalPrice": 200.00,
            "items": [
                {
                    "productId": "prod1",
                    "productName": "Product 1",
                    "quantity": 2,
                    "pricePerUnit": 100.00,
                    "totalPrice": 200.00
                }
            ]
        }
        """;

    when(orderService.cancelOrder(orderId))
        .thenReturn(objectMapper.readValue(expectedResponseJson, OrderResponse.class));

    mockMvc.perform(put("/api/orders/{id}/cancel", orderId))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponseJson));
  }
}