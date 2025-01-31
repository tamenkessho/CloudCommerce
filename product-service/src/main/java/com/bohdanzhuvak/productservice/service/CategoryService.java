package com.bohdanzhuvak.productservice.service;

import com.bohdanzhuvak.productservice.dto.CategoryRequest;
import com.bohdanzhuvak.productservice.dto.CategoryResponse;
import com.bohdanzhuvak.productservice.model.Category;
import com.bohdanzhuvak.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public void createCategory(CategoryRequest categoryRequest) {
    Category category = Category.builder()
            .name(categoryRequest.name())
            .build();
    categoryRepository.save(category);
    log.info("Category {} is saved", category.getId());
  }

  public List<CategoryResponse> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    log.info("Retrieved {} categories", categories.size());
    return categories.stream().map(this::mapToProductDTO).collect(Collectors.toList());
  }

  private CategoryResponse mapToProductDTO(Category category) {
    return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
  }
}
