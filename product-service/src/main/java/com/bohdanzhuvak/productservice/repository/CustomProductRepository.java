package com.bohdanzhuvak.productservice.repository;

import com.bohdanzhuvak.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CustomProductRepository {
  Page<Product> findProductsByFilters(Map<String, String> filters, Pageable pageable);
}
