package com.bohdanzhuvak.userservice.controller;

import com.bohdanzhuvak.userservice.dto.LoginRequest;
import com.bohdanzhuvak.userservice.dto.TokenResponse;
import com.bohdanzhuvak.userservice.dto.UserRequest;
import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse register(@RequestBody @Valid UserRequest request) {
    return userService.register(request);
  }

  @PostMapping("/login")
  public TokenResponse login(@RequestBody @Valid LoginRequest request) {
    return userService.login(request);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserResponse getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.getUserById(userDetails.getUsername());
  }
}
