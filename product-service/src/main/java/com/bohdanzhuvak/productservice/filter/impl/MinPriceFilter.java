package com.bohdanzhuvak.productservice.filter.impl;

import com.bohdanzhuvak.productservice.filter.CriteriaFilter;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class MinPriceFilter implements CriteriaFilter {

  private static final String FILTER_KEY = "minPrice";
  private static final String FIELD_NAME = "price";

  @Override
  public Optional<Criteria> createCriteria(String key, String value) {
    if (!FILTER_KEY.equals(key)) {
      return Optional.empty();
    }

    try {
      BigDecimal minPrice = new BigDecimal(value);
      return Optional.of(Criteria.where(FIELD_NAME).gte(minPrice.doubleValue()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }
}
