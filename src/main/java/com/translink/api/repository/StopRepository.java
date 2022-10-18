package com.translink.api.repository;

import com.translink.api.repository.addOn.StopRepositoryCustom;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.embed.Days;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends MongoRepository<Stop, String>, StopRepositoryCustom {

}
