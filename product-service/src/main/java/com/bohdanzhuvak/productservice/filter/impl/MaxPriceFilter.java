package com.bohdanzhuvak.productservice.filter.impl;

import com.bohdanzhuvak.productservice.filter.CriteriaFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class MaxPriceFilter implements CriteriaFilter {
  private final String FILTER_KEY = "maxPrice";
  private final String FIELD_NAME = "price";

  @Override
  public Optional<Criteria> createCriteria(String key, String value) {
    if (!FILTER_KEY.equals(key)) {
      return Optional.empty();
    }

    try {
      BigDecimal maxPrice = new BigDecimal(value);
      return Optional.of(Criteria.where(FIELD_NAME).lte(maxPrice.doubleValue()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }
}
