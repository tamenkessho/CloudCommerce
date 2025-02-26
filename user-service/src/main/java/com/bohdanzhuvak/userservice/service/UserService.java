package com.bohdanzhuvak.userservice.service;

import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.model.User;
import com.bohdanzhuvak.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;

  public UserResponse getUserById(String id) {
    return userRepository.findById(id).map(this::mapToResponse)
        .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
  }

  private UserResponse mapToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .roles(user.getRoles())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
