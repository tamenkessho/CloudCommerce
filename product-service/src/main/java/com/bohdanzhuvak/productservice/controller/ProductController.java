package com.bohdanzhuvak.productservice.controller;

import com.bohdanzhuvak.productservice.dto.ProductRequest;
import com.bohdanzhuvak.productservice.dto.ProductResponse;
import com.bohdanzhuvak.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;

  @PostMapping
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
    log.info("Post /api/products: {}", productRequest);
    return productService.createProduct(productRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public PagedModel<EntityModel<ProductResponse>> getAllProducts(Pageable pageable,
                                                         @RequestParam(required = false) Map<String, String> filterParams,
                                                         PagedResourcesAssembler<ProductResponse> assembler) {
    log.info("Get /api/products");
    return assembler.toModel(productService.getProducts(pageable, filterParams));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse getProduct(@PathVariable String id) {
    log.info("Get /api/products/{}", id);
    return productService.getProductById(id);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse updateProduct(@PathVariable String id,
                                       @RequestBody ProductRequest productRequest) {
    log.info("Put /api/products/{}", id);
    return productService.updateProductById(id, productRequest);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable String id) {
    log.info("Delete /api/products/{}", id);
    productService.deleteProductById(id);
  }

}
