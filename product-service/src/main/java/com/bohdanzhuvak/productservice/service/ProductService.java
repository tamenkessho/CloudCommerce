package com.bohdanzhuvak.productservice.service;

import com.bohdanzhuvak.commonexceptions.exception.ProductNotFoundException;
import com.bohdanzhuvak.productservice.dto.ProductRequest;
import com.bohdanzhuvak.productservice.dto.ProductResponse;
import com.bohdanzhuvak.productservice.mapper.ProductMapper;
import com.bohdanzhuvak.productservice.model.Product;
import com.bohdanzhuvak.productservice.repository.CategoryRepository;
import com.bohdanzhuvak.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = Optional.of(productRequest)
        .map(request -> categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new ProductNotFoundException("Category " + productRequest.categoryId() + " not found")))
        .map(category -> productMapper.toProduct(productRequest, category))
        .map(productRepository::save)
        .orElseThrow(() -> new ProductNotFoundException("Product not found"));

    log.info("Product {} is saved", product.getId());

    return productMapper.toProductResponse(product);
  }

  public ProductResponse getProductById(String productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("Product " + productId + " not found"));

    log.info("Product {} is found", productId);

    return productMapper.toProductResponse(product);
  }

  public Page<ProductResponse> getProducts(Pageable pageable, Map<String, String> filterParams) {
    Page<Product> products = productRepository.findProductsByFilters(filterParams, pageable);

    log.info("List of {} products filtered by {} is found", products.getTotalElements(), filterParams);

    return products.map(productMapper::toProductResponse);
  }

  public ProductResponse updateProductById(String id, ProductRequest productRequest) {
    Product product = productRepository.findById(id)
        .map(existingProduct -> productMapper.copyRequestToProduct(productRequest, existingProduct))
        .map(productRepository::save)
        .orElseThrow(() -> new ProductNotFoundException("Product " + id + " not found"));

    log.info("Product {} is updated", id);

    return productMapper.toProductResponse(product);
  }

  public void deleteProductById(String productId) {
    productRepository.findById(productId)
        .ifPresentOrElse(
            product -> productRepository.deleteById(productId),
            () -> { throw new ProductNotFoundException("Product " + productId + " not found"); }
        );

    log.info("Product {} is deleted", productId);
  }
}
