package com.bohdanzhuvak.productservice.filter;

import com.bohdanzhuvak.productservice.filter.impl.CategoryFilter;
import com.bohdanzhuvak.productservice.filter.impl.MaxPriceFilter;
import com.bohdanzhuvak.productservice.filter.impl.MinPriceFilter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

class PriceAndCategoryFiltersTest {

  private static Stream<Arguments> provideFilters() {
    return Stream.of(
        Arguments.of(new MaxPriceFilter(), "maxPrice", "price"),
        Arguments.of(new MinPriceFilter(), "minPrice", "price"),
        Arguments.of(new CategoryFilter(), "category", "category")
    );
  }

  @ParameterizedTest
  @MethodSource("provideFilters")
  void shouldReturnCriteriaWhenValidKeyAndValue(
      CriteriaFilter filter, String validKey, String fieldName
  ) {
    // Arrange
    String validValue = fieldName.equals("category") ? "electronics" : "100";

    // Act
    Optional<Criteria> result = filter.createCriteria(validKey, validValue);

    // Assert
    assertThat(result).isPresent();
    assertThat(result.get().getCriteriaObject().toString())
        .contains(fieldName);
  }

  @ParameterizedTest
  @MethodSource("provideFilters")
  void shouldReturnEmptyForWrongKey(
      CriteriaFilter filter, String validKey, String fieldName
  ) {
    // Arrange
    String wrongKey = validKey + "Invalid";
    String validValue = fieldName.equals("category") ? "electronics" : "100";

    // Act
    Optional<Criteria> result = filter.createCriteria(wrongKey, validValue);

    // Assert
    assertThat(result).isEmpty();
  }

  static Stream<Arguments> priceFiltersProvider() {
    return Stream.of(
        Arguments.of(new MaxPriceFilter(), "maxPrice"),
        Arguments.of(new MinPriceFilter(), "minPrice")
    );
  }

  @ParameterizedTest
  @MethodSource("priceFiltersProvider")
  void shouldHandleDifferentPriceValues(CriteriaFilter filter, String key) {
    // Arrange
    String[] values = {
        "999.99",
        "1000000"
    };

    for (String value : values) {
      // Act
      Optional<Criteria> result = filter.createCriteria(key, value);

      // Assert
      assertThat(result)
          .isPresent()
          .hasValueSatisfying(c ->
              assertThat(c.getCriteriaObject().toString())
                  .contains("price")
          );
    }
  }

  @ParameterizedTest
  @MethodSource("categoryValues")
  void categoryFilterShouldHandleDifferentFormats(String value, boolean expected) {
    // Arrange
    CategoryFilter filter = new CategoryFilter();

    // Act
    Optional<Criteria> result = filter.createCriteria("category", value);

    // Assert
    assertThat(result.isPresent()).isEqualTo(expected);
  }

  private static Stream<Arguments> categoryValues() {
    return Stream.of(
        Arguments.of("Electronics", true),
        Arguments.of("  electronics  ", true),
        Arguments.of("", false),
        Arguments.of(null, false),
        Arguments.of("123", true)
    );
  }

  @ParameterizedTest
  @MethodSource("boundaryPriceValues")
  void priceFiltersShouldHandleBoundaryValues(
      CriteriaFilter filter, String key, String value, boolean expected
  ) {
    // Act
    Optional<Criteria> result = filter.createCriteria(key, value);

    // Assert
    assertThat(result.isPresent()).isEqualTo(expected);
  }

  private static Stream<Arguments> boundaryPriceValues() {
    return Stream.of(
        Arguments.of(new MaxPriceFilter(), "maxPrice", "0", true),
        Arguments.of(new MinPriceFilter(), "minPrice", "999999.99", true)
    );
  }
}