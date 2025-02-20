package com.bohdanzhuvak.productservice.filter.factory;

import com.bohdanzhuvak.productservice.filter.CriteriaFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CriteriaFilterFactory {
  private final List<CriteriaFilter> filters;

  public List<Criteria> createFilters(Map<String, String> filterParams) {
    return filterParams.entrySet().stream()
        .flatMap(entry -> filters.stream()
            .map(filter -> filter.createCriteria(entry.getKey(), entry.getValue()))
            .filter(Optional::isPresent)
            .map(Optional::get)
        )
        .collect(Collectors.toList());
  }
}
