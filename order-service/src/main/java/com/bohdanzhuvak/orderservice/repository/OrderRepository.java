package com.bohdanzhuvak.orderservice.repository;

import com.bohdanzhuvak.orderservice.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
  List<Order> findByUserId(String userId);
}
