package com.bohdanzhuvak.commonsecurity.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.bohdanzhuvak.commonsecurity.filter.HeaderAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class DefaultSecurityConfig {

  private final HeaderAuthenticationFilter headerAuthenticationFilter;

  @Bean
  static RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("MODERATOR")
        .role("MODERATOR").implies("USER")
        .build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
