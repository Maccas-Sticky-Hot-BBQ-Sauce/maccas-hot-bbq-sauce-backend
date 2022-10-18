package com.translink.api.repository;

import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.embed.Days;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends MongoRepository<Stop, String> {
    
    @Aggregation(pipeline = {
            "{$match: { \"_id\" : \"4970\"}}",
            "{$lookup: { from: \"stopTime\", localField: \"stopTimes\", foreignField: \"_id\", as: \"stopTimes\"}}",
            "{$project: { _id : 1, stopCode : 1, name : 1, description : 1, latitude : 1, longitude : 1, zoneId : 1, stopUrl : 1, locationType : 1, stopTimes : { $filter : { input : \"$stopTimes\", as : \"stopTime\", cond : { $and : [{$in : [\"MONDAY\",\"$$stopTime.days\"]}, {$gte : [\"$$stopTime.departure.specializedTime\", \"19:00:00\"]}, {$lte : [\"$$stopTime.departure.specializedTime\", \"23:59:59\"]}]}}}, childStops : 1, _class : 1}}"
    })
    AggregationResults<Stop> findStopByStopIdFilterByDayAndTime(String stopId, Days days, SpecializedTime betweenFirst, SpecializedTime betweenSecond);
}
