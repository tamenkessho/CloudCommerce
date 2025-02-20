package com.bohdanzhuvak.productservice.filter.impl;

import com.bohdanzhuvak.productservice.filter.CriteriaFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryFilter implements CriteriaFilter {
  private final String FILTER_KEY = "category";
  private final String FIELD_NAME = "category.name";

  @Override
  public Optional<Criteria> createCriteria(String key, String value) {
    if (!(FILTER_KEY.equals(key) && value != null && !value.isEmpty())) {
      return Optional.empty();
    }
    return Optional.of(Criteria.where(FIELD_NAME).is(value));
  }
}
