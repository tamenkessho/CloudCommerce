package com.bohdanzhuvak.productservice.controller;

import com.bohdanzhuvak.productservice.dto.ProductCreateDTO;
import com.bohdanzhuvak.productservice.dto.ProductDTO;
import com.bohdanzhuvak.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
    productService.createProduct(productCreateDTO);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDTO getProduct(@PathVariable String id) {
    return productService.getProduct(id);
  }

}
