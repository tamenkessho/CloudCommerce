package com.bohdanzhuvak.userservice.security;

import com.bohdanzhuvak.userservice.config.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtCookieUtil {
  private final JwtProperties jwtProperties;

  public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setDomain("localhost");
    cookie.setPath("/api/auth");
    cookie.setMaxAge((int) jwtProperties.getRefreshTokenExpiration().getSeconds());
    response.addCookie(cookie);
  }

  public String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) return null;
    return Arrays.stream(request.getCookies())
        .filter(cookie -> "refreshToken".equals(cookie.getName()))
        .findFirst()
        .map(Cookie::getValue)
        .orElse(null);
  }

  public void clearRefreshTokenCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setHttpOnly(true);
    cookie.setDomain("localhost");
    cookie.setPath("/api/auth");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }
}
