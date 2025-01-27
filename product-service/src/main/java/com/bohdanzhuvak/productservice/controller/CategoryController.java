package com.bohdanzhuvak.productservice.controller;

import com.bohdanzhuvak.productservice.dto.CategoryCreateDTO;
import com.bohdanzhuvak.productservice.dto.CategoryDTO;
import com.bohdanzhuvak.productservice.dto.ProductCreateDTO;
import com.bohdanzhuvak.productservice.dto.ProductDTO;
import com.bohdanzhuvak.productservice.repository.CategoryRepository;
import com.bohdanzhuvak.productservice.service.CategoryService;
import com.bohdanzhuvak.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
    categoryService.createCategory(categoryCreateDTO);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<CategoryDTO> getAllCategories() {
    return categoryService.getAllCategories();
  }

}
