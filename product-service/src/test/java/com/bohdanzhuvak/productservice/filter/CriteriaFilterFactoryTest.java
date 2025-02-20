package com.bohdanzhuvak.productservice.filter;

import com.bohdanzhuvak.productservice.filter.factory.CriteriaFilterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriteriaFilterFactoryTest {

  @Mock
  private CriteriaFilter filter1;

  @Mock
  private CriteriaFilter filter2;

  private CriteriaFilterFactory factory;

  @BeforeEach
  void setUp() {
    factory = new CriteriaFilterFactory(List.of(filter1, filter2));
  }

  @Test
  void shouldCombineResultsFromMultipleFilters() {

    lenient().when(filter1.createCriteria(any(), any()))
        .thenReturn(Optional.empty());
    // Arrange
    when(filter1.createCriteria(eq("key1"), any()))
        .thenReturn(Optional.of(Criteria.where("field1").is("value1")));


    lenient().when(filter2.createCriteria(any(), any()))
        .thenReturn(Optional.empty());

    when(filter2.createCriteria(eq("key2"), any()))
        .thenReturn(Optional.of(Criteria.where("field2").gt(10)));

    Map<String, String> params = Map.of(
        "key1", "value1",
        "key2", "20",
        "invalidKey", "invalidValue"
    );

    // Act
    List<Criteria> result = factory.createFilters(params);

    // Assert
    assertThat(result)
        .hasSize(2)
        .containsExactlyInAnyOrder(
            Criteria.where("field1").is("value1"),
            Criteria.where("field2").gt(10)
        );
  }

  @Test
  void shouldIgnoreUnprocessedParams() {
    // Arrange
    when(filter1.createCriteria(any(), any())).thenReturn(Optional.empty());

    Map<String, String> params = Map.of(
        "unknownKey1", "value1",
        "unknownKey2", "123"
    );

    // Act
    List<Criteria> result = factory.createFilters(params);

    // Assert
    assertThat(result).isEmpty();
  }
}