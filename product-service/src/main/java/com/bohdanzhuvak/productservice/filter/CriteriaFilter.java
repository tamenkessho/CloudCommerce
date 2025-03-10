package com.bohdanzhuvak.productservice.filter;

import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;

public interface CriteriaFilter {

  Optional<Criteria> createCriteria(String key, String value);
}
