package com.bohdanzhuvak.userservice.controller;

import com.bohdanzhuvak.userservice.dto.LoginRequest;
import com.bohdanzhuvak.userservice.dto.TokenResponse;
import com.bohdanzhuvak.userservice.dto.UserRequest;
import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse register(@RequestBody @Valid UserRequest request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  public TokenResponse login(@RequestBody @Valid LoginRequest request,
      HttpServletResponse response) {
    return authService.login(request, response);
  }

  @PostMapping("/refresh")
  public TokenResponse refreshAccessToken(HttpServletRequest request,
      HttpServletResponse response) {
    return authService.refreshAccessToken(request, response);
  }
}

