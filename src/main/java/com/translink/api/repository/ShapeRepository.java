package com.translink.api.repository;

import com.translink.api.repository.model.Shape;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShapeRepository extends MongoRepository<Shape, String> {
}
