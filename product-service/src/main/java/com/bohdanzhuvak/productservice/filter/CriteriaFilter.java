package com.bohdanzhuvak.productservice.filter;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Optional;

public interface CriteriaFilter {
  Optional<Criteria> createCriteria(String key, String value);
}
