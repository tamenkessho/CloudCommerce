package com.bohdanzhuvak.productservice.repository;

import com.bohdanzhuvak.productservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

}
