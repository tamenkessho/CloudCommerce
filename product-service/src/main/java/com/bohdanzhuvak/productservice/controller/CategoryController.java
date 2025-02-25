package com.bohdanzhuvak.productservice.controller;

import com.bohdanzhuvak.productservice.dto.CategoryRequest;
import com.bohdanzhuvak.productservice.dto.CategoryResponse;
import com.bohdanzhuvak.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
    log.info("Post /api/categories: {}", categoryRequest);
    return categoryService.createCategory(categoryRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<CategoryResponse> getAllCategories() {
    log.info("Get /api/categories");
    return categoryService.getAllCategories();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryResponse getCategory(@PathVariable String id){
    log.info("Get /api/categories/{}", id);
    return categoryService.getCategory(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryResponse updateCategory(@PathVariable String id,
                                         @RequestBody CategoryRequest categoryRequest) {
    log.info("Put /api/categories/{}: {}", id, categoryRequest);
    return categoryService.updateCategory(id, categoryRequest);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable String id) {
    log.info("Delete /api/categories/{}", id);
    categoryService.deleteCategory(id);
  }

}
