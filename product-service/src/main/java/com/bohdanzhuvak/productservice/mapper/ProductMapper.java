package com.bohdanzhuvak.productservice.mapper;

import com.bohdanzhuvak.productservice.dto.ProductRequest;
import com.bohdanzhuvak.productservice.dto.ProductResponse;
import com.bohdanzhuvak.productservice.model.Category;
import com.bohdanzhuvak.productservice.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductResponse toProductResponse(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "category", ignore = true)
  void updateProduct(ProductRequest productRequest, @MappingTarget Product existingProduct);


  default Product updateProductWithoutMutation(ProductRequest productRequest, Product product){
    Product copy = product.toBuilder().build();
    updateProduct(productRequest, copy);
    return copy;
  }

  @Mapping(target = "category", source = "category")
  @Mapping(target = "name", source = "productRequest.name")
  @Mapping(target = "id", ignore = true)
  Product toProduct(ProductRequest productRequest, Category category);

  default String map(Category category) {
    return category != null ? category.getName() : null;
  }
}
