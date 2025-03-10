package com.bohdanzhuvak.orderservice.repository;

import com.bohdanzhuvak.orderservice.model.Order;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

  List<Order> findByUserId(String userId);
}
