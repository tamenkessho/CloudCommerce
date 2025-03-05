package com.bohdanzhuvak.userservice.service;

import com.bohdanzhuvak.commonexceptions.exception.impl.InvalidCredentialsException;
import com.bohdanzhuvak.commonexceptions.exception.impl.InvalidTokenException;
import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.commonexceptions.exception.impl.UserAlreadyExistsException;
import com.bohdanzhuvak.userservice.dto.LoginRequest;
import com.bohdanzhuvak.userservice.dto.TokenResponse;
import com.bohdanzhuvak.userservice.dto.UserRequest;
import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.mapper.UserMapper;
import com.bohdanzhuvak.userservice.model.Role;
import com.bohdanzhuvak.userservice.model.User;
import com.bohdanzhuvak.userservice.repository.UserRepository;
import com.bohdanzhuvak.userservice.security.JwtCookieUtil;
import com.bohdanzhuvak.userservice.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;
  private final JwtCookieUtil cookieUtil;
  private final UserDetailsService userDetailsService;
  private final UserMapper userMapper;

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
    return userMapper.toResponse(user);
  }

  public TokenResponse login(LoginRequest request, HttpServletResponse response) {
    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new ResourceNotFoundException("User with email " + request.email() + " not found"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new InvalidCredentialsException();
    }
    String refreshToken = tokenProvider.generateRefreshToken(user);
    cookieUtil.addRefreshTokenCookie(response, refreshToken);

    return tokenProvider.generateAccessToken(user);
  }

  public TokenResponse refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = cookieUtil.extractRefreshTokenFromCookie(request);

    if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) {
      cookieUtil.clearRefreshTokenCookie(response);
      throw new InvalidTokenException();
    }

    String userId = tokenProvider.getUserIdFromToken(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

    return tokenProvider.generateAccessToken(userDetails);
  }
}
