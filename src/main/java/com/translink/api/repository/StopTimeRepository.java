package com.translink.api.repository;

import com.translink.api.repository.model.StopTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopTimeRepository extends MongoRepository<StopTime, String> {
}
