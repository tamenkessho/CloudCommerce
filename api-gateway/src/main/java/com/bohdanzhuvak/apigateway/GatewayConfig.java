package com.bohdanzhuvak.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthFilter) {
    return builder.routes()
        .route("user-service-with-filter", r -> r.path("/api/users/**")
            .filters(f -> f.filter(jwtAuthFilter))
            .uri("http://localhost:8083"))

        .route("user-service-no-filter", r -> r.path("/api/auth/**")
            .uri("http://localhost:8083"))

        .route("order-service", r -> r.path("/api/orders/**")
            .filters(f -> f.filter(jwtAuthFilter))
            .uri("http://localhost:8082"))

        .route("product-service", r -> r.path("/api/products/**")
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
        .build();
  }
}
