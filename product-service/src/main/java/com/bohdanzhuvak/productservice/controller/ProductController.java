package com.bohdanzhuvak.productservice.controller;

import com.bohdanzhuvak.productservice.dto.ProductRequest;
import com.bohdanzhuvak.productservice.dto.ProductResponse;
import com.bohdanzhuvak.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
    log.info("Post /api/products: {}", productRequest);
    return productService.createProduct(productRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductResponse> getAllProducts() {
    log.info("Get /api/products");
    return productService.getAllProducts();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse getProduct(@PathVariable String id) {
    log.info("Get /api/products/{}", id);
    return productService.getProductById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable String id) {
    log.info("Delete /api/products/{}", id);
    productService.deleteProductById(id);
  }

}
