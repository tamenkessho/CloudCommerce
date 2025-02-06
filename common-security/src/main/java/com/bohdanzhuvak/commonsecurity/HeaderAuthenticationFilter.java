package com.bohdanzhuvak.commonsecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  )
      throws ServletException, IOException {

    String userId = request.getHeader("X-User-Id");
    String rolesHeader = request.getHeader("X-User-Roles");
    if (userId != null && rolesHeader != null) {
      Set<Role> roles = Arrays.stream(rolesHeader.split(","))
          .map(String::trim)
          .map(role -> {
            try {
              return Role.valueOf(role);
            } catch (IllegalArgumentException e) {
              log.warn("Invalid role received: {}", role);
              return null;
            }
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());

      Collection<? extends GrantedAuthority> authorities = roles.stream()
          .map(role -> new SimpleGrantedAuthority(role.name()))
          .collect(Collectors.toList());

      User user = new User(userId, "", authorities);
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
