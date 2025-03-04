package com.bohdanzhuvak.userservice.controller;

import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  @DeleteMapping("{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasRole('USER')")
  public void deleteUser(@PathVariable("id") String id) {
    userService.deleteUserById(id);
  }
}
