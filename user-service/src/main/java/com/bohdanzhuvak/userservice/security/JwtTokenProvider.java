package com.bohdanzhuvak.userservice.security;

import com.bohdanzhuvak.userservice.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;

  public String generateAccessToken(UserDetails userDetails) {
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
    return buildToken(Map.of("roles", roles), userDetails, jwtProperties.getAccessTokenExpiration());
  }

  public String generateRefreshToken(UserDetails userDetails) {
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return buildToken(Map.of("roles", roles), userDetails, jwtProperties.getRefreshTokenExpiration());
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      long expiration
  ) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(jwtProperties.getSecretKey())
        .compact();
  }

  public boolean validateToken(String token) {
    return !extractClaim(token, Claims::getExpiration).before(new Date());
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