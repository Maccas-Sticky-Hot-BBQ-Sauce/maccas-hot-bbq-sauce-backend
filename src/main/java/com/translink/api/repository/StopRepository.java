package com.translink.api.repository;

import com.translink.api.repository.model.Stop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends MongoRepository<Stop, String> {
}
