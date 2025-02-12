package com.bohdanzhuvak.apigateway.security;

import com.bohdanzhuvak.apigateway.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter {
  private final JwtTokenProvider jwtTokenProvider;
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String jwt = getJwtFromRequest(exchange.getRequest());
    if (!StringUtils.hasText(jwt)) {
      return chain.filter(exchange);
    }
    if (!jwtTokenProvider.validateToken(jwt)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    UserDTO userDTO = jwtTokenProvider.getUserFromToken(jwt);

    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
        .header("X-User-Id", userDTO.userId())
        .header("X-User-Roles", userDTO.roles())
        .build();

    return chain.filter(exchange.mutate().request(modifiedRequest).build());
  }

  private String getJwtFromRequest(ServerHttpRequest request) {
    String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ?
        bearerToken.substring(7) :
        null;
  }
}
