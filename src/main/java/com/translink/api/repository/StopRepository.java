package com.translink.api.repository;

import com.translink.api.repository.addOn.StopRepositoryCustom;
import com.translink.api.repository.model.Stop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends MongoRepository<Stop, String>, StopRepositoryCustom {
//    @Aggregation(pipeline = {
//            "{$match: {\"stopId\": ?0}}",
//            "{$lookup: {from: \"stopTime\", localField: \"stopTimes\", foreignField: \"_id\", as: \"stopTimes\"}}",
//            "{$project: {_id : 1, stopId : 1, stopCode : 1, name : 1, description : 1, latitude : 1, longitude : 1, zoneId : 1, stopUrl : 1, locationType : 1, stopTimes : { $filter : { input : \"$stopTimes\", as : \"stopTime\", cond : { $and : [{$gte : [\"$$stopTime.departure.specializedTime\", ?2]}, {$lte : [\"$$stopTime.departure.specializedTime\", ?3]}]}}}, childStops : 1, _class : 1}}",
//            "{$unwind: {path: \"$stopTimes\"}}",
//            "{$lookup: {from: \"trip\", localField: \"stopTimes.trip\", foreignField: \"_id\", as: \"stopTimes.trip\"}}",
//            "{$unwind: {path: \"$stopTimes.trip\"}}",
//            "{$unwind: {path: \"$stopTimes.trip.calendar.days\"}}",
//            "{$match: {\"stopTimes.trip.calendar.days\": ?1}}",
//            "{$set: {\"stopTimes.trip.calendar.days\": [\"$stopTimes.trip.calendar.days\"]}}",
//            "{$group: { _id: \"$_id\", _class: { $first: \"$_class\" }, childStops: { $first: \"$childStops\" }, description: { $first: \"$description\" }, latitude: { $first: \"$latitude\" }, longitude: { $first: \"$longitude\" }, name: { $first: \"$name\" }, stopCode: { $first: \"$stopCode\" }, stopId: { $first: \"$stopId\" }, stopUrl: { $first: \"$stopUrl\" }, zoneId: { $first: \"$zoneId\" }, stopTimes: { $push: \"$stopTimes\" }}}"
//    })
//    Stop findStopByStopIdFilterByDayAndTime(String stopId, Days days, SpecializedTime betweenFirst, SpecializedTime betweenSecond);
}
