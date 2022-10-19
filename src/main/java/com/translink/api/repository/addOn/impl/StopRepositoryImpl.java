package com.translink.api.repository.addOn.impl;

import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.addOn.StopRepositoryCustom;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.embed.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StopRepositoryImpl implements StopRepositoryCustom {
    private MongoTemplate template;

    @Autowired
    public void setTemplate(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public Stop findStopByStopIdFilterByDayAndTime(String stopId, Days days, SpecializedTime fromTime, SpecializedTime toTime) {
        Stop stop = template.findOne(new Query(Criteria.where("id").is(stopId)), Stop.class);
        stop.setStopTimes(
                stop.getStopTimes().parallelStream()
                        .filter(stopTime -> {
                            String time = stopTime.getDeparture().toString();

                            return time.compareTo(fromTime.toString()) >= 0 && time.compareTo(toTime.toString()) <= 0;
                        })
                        .filter(stopTime -> stopTime.getTrip().getCalendar().getDays().contains(days))
                        .collect(Collectors.toList())
        );

        return stop;
    }
}
