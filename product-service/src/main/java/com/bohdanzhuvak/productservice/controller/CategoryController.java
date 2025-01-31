package com.bohdanzhuvak.productservice.controller;

import com.bohdanzhuvak.productservice.dto.CategoryRequest;
import com.bohdanzhuvak.productservice.dto.CategoryResponse;
import com.bohdanzhuvak.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createCategory(@RequestBody CategoryRequest categoryRequest) {
    log.info("Post /api/categories: {}", categoryRequest);
    categoryService.createCategory(categoryRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<CategoryResponse> getAllCategories() {
    log.info("Get /api/categories");
    return categoryService.getAllCategories();
  }

}
