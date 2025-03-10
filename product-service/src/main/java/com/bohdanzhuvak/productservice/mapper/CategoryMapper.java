package com.bohdanzhuvak.productservice.mapper;

import com.bohdanzhuvak.productservice.dto.CategoryRequest;
import com.bohdanzhuvak.productservice.dto.CategoryResponse;
import com.bohdanzhuvak.productservice.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  CategoryResponse toCategoryResponse(Category category);

  Category toCategory(CategoryRequest categoryRequest);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "id", ignore = true)
  void applyRequestToCategory(CategoryRequest categoryRequest,
      @MappingTarget Category existingCategory);

  default Category copyRequestToCategory(CategoryRequest categoryRequest,
      Category existingCategory) {
    Category copy = existingCategory.toBuilder().build();
    applyRequestToCategory(categoryRequest, copy);
    return copy;
  }
}
