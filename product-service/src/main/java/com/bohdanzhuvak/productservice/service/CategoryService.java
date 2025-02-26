package com.bohdanzhuvak.productservice.service;

import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.productservice.dto.CategoryRequest;
import com.bohdanzhuvak.productservice.dto.CategoryResponse;
import com.bohdanzhuvak.productservice.mapper.CategoryMapper;
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
  private final CategoryMapper categoryMapper;

  public CategoryResponse createCategory(CategoryRequest categoryRequest) {
    Category category = categoryRepository.save(categoryMapper.toCategory(categoryRequest));

    log.info("Category {} is saved", category.getId());

    return categoryMapper.toCategoryResponse(category);
  }

  public List<CategoryResponse> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();

    log.info("Retrieved {} categories", categories.size());

    return categories.stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
  }

  public CategoryResponse getCategory(String id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

    log.info("Category {} is retrieved", category.getId());

    return categoryMapper.toCategoryResponse(category);
  }

  public CategoryResponse updateCategory(String id, CategoryRequest categoryRequest) {
    Category categoryResponse = categoryRepository.findById(id)
        .map(existingCategory -> categoryMapper.copyRequestToCategory(categoryRequest, existingCategory))
        .map(categoryRepository::save)
        .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

    log.info("Category {} is updated", id);

    return categoryMapper.toCategoryResponse(categoryResponse);
  }

  public void deleteCategory(String id) {
    categoryRepository.findById(id)
        .ifPresentOrElse(category -> categoryRepository.deleteById(id),
            () -> {
              log.info("Category with id {} not found", id);
              throw new ResourceNotFoundException("Category with id " + id + " not found");
            }
        );
  }
}
