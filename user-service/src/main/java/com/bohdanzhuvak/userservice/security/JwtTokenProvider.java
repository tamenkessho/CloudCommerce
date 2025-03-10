package com.bohdanzhuvak.userservice.security;

import com.bohdanzhuvak.userservice.config.JwtProperties;
import com.bohdanzhuvak.userservice.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;

  public TokenResponse generateAccessToken(UserDetails userDetails) {
    String roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
    Instant expiresAt = Instant.now().plus(jwtProperties.getAccessTokenExpiration());
    return new TokenResponse(buildToken(Map.of("roles", roles), userDetails, expiresAt), expiresAt);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    String roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
    Instant expiresAt = Instant.now().plus(jwtProperties.getRefreshTokenExpiration());
    return buildToken(Map.of("roles", roles), userDetails, expiresAt);
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      Instant expiresAt
  ) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(expiresAt))
        .signWith(jwtProperties.getSecretKey())
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      return !extractClaim(token, Claims::getExpiration).before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public String getUserIdFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(jwtProperties.getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

}