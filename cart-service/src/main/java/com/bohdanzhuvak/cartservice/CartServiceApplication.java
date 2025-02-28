package com.bohdanzhuvak.cartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableMongoAuditing
@EnableFeignClients
public class CartServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CartServiceApplication.class, args);
  }

}
