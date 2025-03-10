package com.bohdanzhuvak.userservice.service;

import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.mapper.UserMapper;
import com.bohdanzhuvak.userservice.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserResponse getUserById(String id) {
    return userRepository.findById(id).map(userMapper::toResponse)
        .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
  }

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream().map(userMapper::toResponse)
        .collect(Collectors.toList());
  }

  public void deleteUserById(String id) {
    userRepository.findById(id)
        .ifPresentOrElse(
            userRepository::delete,
            () -> {
              throw new ResourceNotFoundException("User with id " + id + " not found");
            }
        );
  }
}
