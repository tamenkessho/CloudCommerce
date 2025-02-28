package com.bohdanzhuvak.cartservice.client;

import com.bohdanzhuvak.cartservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductClient {

  @GetMapping("/api/products/{productId}")
  ProductResponse getProductById(@PathVariable String productId);

}
