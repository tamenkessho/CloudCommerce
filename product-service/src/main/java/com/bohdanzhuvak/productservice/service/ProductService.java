package com.bohdanzhuvak.productservice.service;

import com.bohdanzhuvak.productservice.dto.ProductCreateDTO;
import com.bohdanzhuvak.productservice.dto.ProductDTO;
import com.bohdanzhuvak.productservice.exceptions.NotFoundException;
import com.bohdanzhuvak.productservice.model.Category;
import com.bohdanzhuvak.productservice.model.Product;
import com.bohdanzhuvak.productservice.repository.CategoryRepository;
import com.bohdanzhuvak.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public void createProduct(ProductCreateDTO productCreateDTO) {
    Product product = Product.builder()
            .name(productCreateDTO.getName())
            .description(productCreateDTO.getDescription())
            .categoryId(productCreateDTO.getCategoryId())
            .price(productCreateDTO.getPrice())
            .build();

    productRepository.save(product);
    log.info("Product {} is saved", product.getId());
  }

  public ProductDTO getProduct(String productId) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product " + productId + " not found"));
    log.info("Found product: {}", productId);

    Category category = categoryRepository.findById(product.getCategoryId())
            .orElseThrow(() -> new NotFoundException("Category " + product.getCategoryId() + " not found"));
    log.info("Found category: {}", product.getCategoryId());

    return mapToProductDTO(product, category);
  }

  public List<ProductDTO> getAllProducts() {
    List<Product> products = productRepository.findAll();

    return products.stream()
            .map(product -> {
              Category category = categoryRepository.findById(product.getCategoryId())
                      .orElseThrow(() -> new NotFoundException("Category " + product.getCategoryId() + " not found"));
              return mapToProductDTO(product, category);
            })
            .collect(Collectors.toList());
  }

  private ProductDTO mapToProductDTO(Product product, Category category) {
    return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .category(category.getName())
            .price(product.getPrice())
            .build();
  }
}
