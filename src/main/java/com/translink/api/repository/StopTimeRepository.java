package com.translink.api.repository;

import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.StopTime;
import com.translink.api.repository.model.Trip;
import com.translink.api.repository.model.embed.Days;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StopTimeRepository extends MongoRepository<StopTime, String> {
}
