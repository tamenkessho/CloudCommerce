package com.bohdanzhuvak.userservice.service;

import com.bohdanzhuvak.userservice.dto.LoginRequest;
import com.bohdanzhuvak.userservice.dto.TokenResponse;
import com.bohdanzhuvak.userservice.dto.UserRequest;
import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.exception.InvalidCredentialsException;
import com.bohdanzhuvak.userservice.exception.UserAlreadyExistsException;
import com.bohdanzhuvak.userservice.exception.UserNotFoundException;
import com.bohdanzhuvak.userservice.model.Role;
import com.bohdanzhuvak.userservice.model.User;
import com.bohdanzhuvak.userservice.repository.UserRepository;
import com.bohdanzhuvak.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  @Transactional
  public UserResponse register(UserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new UserAlreadyExistsException(request.email());
    }

    User user = User.builder()
            .email(request.email())
            .passwordHash(passwordEncoder.encode(request.password()))
            .firstName(request.firstName())
            .lastName(request.lastName())
            .roles(Set.of(Role.ROLE_USER))
            .build();

    user = userRepository.save(user);
    log.info("User registered: {}", user.getEmail());
    return mapToResponse(user);
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

  public TokenResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException(request.email()));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new InvalidCredentialsException();
    }

    String accessToken = tokenProvider.generateAccessToken(user);
    String refreshToken = tokenProvider.generateRefreshToken(user);

    return new TokenResponse(
            accessToken,
            refreshToken,
            Instant.now().plus(Duration.ofHours(1))
    );
  }

  public UserResponse getUserById(String id) {
    return userRepository.findById(id).map(this::mapToResponse)
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}
