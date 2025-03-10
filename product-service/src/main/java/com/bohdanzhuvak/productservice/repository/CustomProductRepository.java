package com.bohdanzhuvak.productservice.repository;

import com.bohdanzhuvak.productservice.model.Product;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {

  Page<Product> findProductsByFilters(Map<String, String> filters, Pageable pageable);
}
