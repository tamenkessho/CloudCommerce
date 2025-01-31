package com.bohdanzhuvak.productservice.service;

import com.bohdanzhuvak.productservice.dto.ProductRequest;
import com.bohdanzhuvak.productservice.dto.ProductResponse;
import com.bohdanzhuvak.productservice.exceptions.ProductNotFoundException;
import com.bohdanzhuvak.productservice.model.Category;
import com.bohdanzhuvak.productservice.model.Product;
import com.bohdanzhuvak.productservice.repository.CategoryRepository;
import com.bohdanzhuvak.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Category category = categoryRepository.findById(productRequest.categoryId()).orElseThrow(() -> new ProductNotFoundException("Category " + productRequest.categoryId() + " not found"));
    Product product = Product.builder()
            .name(productRequest.name())
            .description(productRequest.description())
            .category(category)
            .price(productRequest.price())
            .build();

    product = productRepository.save(product);
    log.info("Product {} is saved", product.getId());
    return mapToProductResponse(product);
  }

  public ProductResponse getProductById(String productId) {
    ProductResponse productResponse = productRepository.findById(productId).map(this::mapToProductResponse)
            .orElseThrow(() -> new ProductNotFoundException("Product " + productId + " not found"));
    log.info("Product {} is found", productId);
    return productResponse;
  }

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();
    log.info("Retrieved {} products", products.size());
    return products.stream()
            .map(this::mapToProductResponse)
            .toList();
  }

  private ProductDTO mapToProductDTO(Product product, Category category) {
    return ProductDTO.builder()
  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .category(product.getCategory().getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
  }
}
