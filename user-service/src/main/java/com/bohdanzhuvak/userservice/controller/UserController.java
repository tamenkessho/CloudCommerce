package com.bohdanzhuvak.userservice.controller;

import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('USER')")
  public UserResponse getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.getUserById(userDetails.getUsername());
  }
}
