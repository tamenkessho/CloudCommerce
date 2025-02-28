package com.bohdanzhuvak.apigateway.config;

import com.bohdanzhuvak.apigateway.security.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthFilter) {
    return builder.routes()
        .route("cart-service", r -> r.path("/api/cart/**")
            .filters(f -> f.filter(jwtAuthFilter))
            .uri("http://localhost:8084"))

        .route("user-service", r -> r.path("/api/users/**", "/api/auth/**")
            .filters(f -> f.filter(jwtAuthFilter))
            .uri("http://localhost:8083"))

        .route("order-service", r -> r.path("/api/orders/**")
            .filters(f -> f.filter(jwtAuthFilter))
            .uri("http://localhost:8082"))

        .route("product-service", r -> r.path("/api/products/**", "/api/categories/**")
            .filters(f -> f.filter(jwtAuthFilter))
            .uri("http://localhost:8081"))

        .route("product-service-swagger", r -> r.path("/api/product-service/v3/api-docs")
            .filters(f -> f.rewritePath("/api/product-service/v3/api-docs", "/v3/api-docs"))
            .uri("http://localhost:8081"))

        .route("order-service-swagger", r -> r.path("/api/order-service/v3/api-docs")
            .filters(f -> f.rewritePath("/api/order-service/v3/api-docs", "/v3/api-docs"))
            .uri("http://localhost:8082"))

        .route("user-service-swagger", r -> r.path("/api/user-service/v3/api-docs")
            .filters(f -> f.rewritePath("/api/user-service/v3/api-docs", "/v3/api-docs"))
            .uri("http://localhost:8083"))

        .route("cart-service-swagger", r -> r.path("/api/cart-service/v3/api-docs")
            .filters(f -> f.rewritePath("/api/cart-service/v3/api-docs", "/v3/api-docs"))
            .uri("http://localhost:8084"))
        .build();
  }
}
