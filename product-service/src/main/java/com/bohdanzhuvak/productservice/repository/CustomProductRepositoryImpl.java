package com.bohdanzhuvak.productservice.repository;

import com.bohdanzhuvak.productservice.filter.factory.CriteriaFilterFactory;
import com.bohdanzhuvak.productservice.model.Product;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

  private final MongoTemplate mongoTemplate;
  private final CriteriaFilterFactory filterFactory;

  @Override
  public Page<Product> findProductsByFilters(Map<String, String> filters, Pageable pageable) {
    Query query = new Query();
    List<Criteria> criteriaList = filterFactory.createFilters(filters);
    if (!criteriaList.isEmpty()) {
      query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
    }
    query.with(pageable);
    List<Product> products = mongoTemplate.find(query, Product.class);

    long total = mongoTemplate.count(query.skip(-1).limit(-1), Product.class);

    return new PageImpl<>(products, pageable, total);
  }
}
