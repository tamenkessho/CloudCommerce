package com.bohdanzhuvak.productservice.filter;

import com.bohdanzhuvak.productservice.filter.factory.CriteriaFilterFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CriteriaFilterFactoryIntegrationTest {

  @Autowired
  private CriteriaFilterFactory factory;

  @Test
  void shouldProcessMultipleFiltersTogether() {
    // Arrange
    Map<String, String> params = Map.of(
        "maxPrice", "1000",
        "category", "electronics",
        "invalidParam", "someValue"
    );

    // Act
    List<Criteria> result = factory.createFilters(params);

    // Assert
    assertThat(result)
        .hasSize(2)
        .contains(
            Criteria.where("price").lte(new BigDecimal("1000").doubleValue()),
            Criteria.where("category.name").is("electronics")
        );
  }
}
