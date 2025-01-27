package com.bohdanzhuvak.productservice.service;

import com.bohdanzhuvak.productservice.dto.CategoryCreateDTO;
import com.bohdanzhuvak.productservice.dto.CategoryDTO;
import com.bohdanzhuvak.productservice.dto.ProductDTO;
import com.bohdanzhuvak.productservice.model.Category;
import com.bohdanzhuvak.productservice.model.Product;
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

  public void createCategory(CategoryCreateDTO categoryCreateDTO) {
    Category category = Category.builder()
            .name(categoryCreateDTO.getName())
            .build();
    categoryRepository.save(category);
    log.info("Category {} is saved", category.getId());
  }

  public List<CategoryDTO> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream().map(this::mapToProductDTO).collect(Collectors.toList());
  }

  private CategoryDTO mapToProductDTO(Category category) {
    return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
  }
}
