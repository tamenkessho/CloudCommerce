package com.bohdanzhuvak.cartservice.repository;

import com.bohdanzhuvak.cartservice.model.Cart;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

  Optional<Cart> findByUserId(String userId);
}