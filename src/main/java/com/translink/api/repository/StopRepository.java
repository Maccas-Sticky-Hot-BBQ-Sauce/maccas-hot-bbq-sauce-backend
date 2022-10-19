package com.translink.api.repository;

import com.translink.api.repository.model.Stop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends MongoRepository<Stop, String> {
    Stop findByStopId(String stopId);

    //@Query("{ 'stopId' : ?0 , 'stopTimes.id' : '633be4df0db4ba4e0f94bb36' }")
    //Stop findByStopIDPeriod(String stopId);
}
