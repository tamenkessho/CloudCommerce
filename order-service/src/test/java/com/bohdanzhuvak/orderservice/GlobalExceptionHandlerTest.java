package com.bohdanzhuvak.orderservice;

import com.bohdanzhuvak.commonexceptions.exception.OrderNotFoundException;
import com.bohdanzhuvak.commonexceptions.exception.ProductNotFoundException;
import com.bohdanzhuvak.orderservice.controller.OrderController;
import com.bohdanzhuvak.orderservice.service.OrderService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackages = {"com.bohdanzhuvak.commonexceptions", "com.bohdanzhuvak.commonsecurity"})
@WithMockUser("user1")
class GlobalExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OrderService orderService;

  @Test
  void handleProductNotFoundException_ShouldReturn404() throws Exception {
    // Arrange
    when(orderService.getOrderById(any()))
            .thenThrow(new ProductNotFoundException("Product not found"));

    // Act & Assert
    mockMvc.perform(get("/api/orders/123"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Product not found"));
  }

  @Test
  void handleOrderNotFoundException_ShouldReturn404() throws Exception {
    // Arrange
    when(orderService.getOrderById(any()))
            .thenThrow(new OrderNotFoundException("Order not found"));

    // Act & Assert
    mockMvc.perform(get("/api/orders/123"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Order not found"));
  }

  @Test
  void handleIllegalStateException_ShouldReturn400() throws Exception {
    // Arrange
    when(orderService.cancelOrder(any()))
            .thenThrow(new IllegalStateException("Order already cancelled"));

    // Act & Assert
    mockMvc.perform(put("/api/orders/123/cancel"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Order already cancelled"));
  }

  @Test
  void handleMethodArgumentNotValidException_ShouldReturn400() throws Exception {
    // Act & Assert (empty request body)
    mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").isArray());
  }

  @Test
  void handleGenericException_ShouldReturn500() throws Exception {
    // Arrange
    when(orderService.getOrderById(any()))
            .thenThrow(new RuntimeException("Internal server error"));

    // Act & Assert
    mockMvc.perform(get("/api/orders/123"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("Internal server error"));
  }

  @Test
  void handleFeignException_ShouldReturn503() throws Exception {
    // Arrange
    when(orderService.createOrder(any()))
            .thenThrow(new FeignException.ServiceUnavailable(
                    "Service unavailable",
                    Request.create(
                            Request.HttpMethod.GET,
                            "/api/products/123",
                            Collections.emptyMap(),
                            null,
                            Charset.defaultCharset(),
                            null
                    ),
                    null,
                    null
            ));

    String validRequest = """
        {
            "items": [{"productId": "prod1", "quantity": 1}]
        }
        """;

    // Act & Assert
    mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRequest))
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.message").value("Dependent service is unavailable"));
  }
}