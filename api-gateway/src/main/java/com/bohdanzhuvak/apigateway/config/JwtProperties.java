package com.bohdanzhuvak.apigateway.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
  @Value("${jwt.secret-key}")
  private String secretKey;

  public SecretKey getSecretKey() {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
