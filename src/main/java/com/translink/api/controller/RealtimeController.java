package com.translink.api.controller;

import com.translink.api.repository.StopTimeRepository;
import com.translink.api.repository.model.StopTime;
import com.translink.api.repository.model.embed.TripUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/realtime")
public class RealtimeController {
    private StopTimeRepository stopTimeRepository;

    @Autowired
    public void setStopTimeRepository(StopTimeRepository stopTimeRepository) {
        this.stopTimeRepository = stopTimeRepository;
    }

    @GetMapping("/trip-update")
    public List<TripUpdate> getTripUpdates(@RequestParam List<String> ids) {
        List<TripUpdate> updates = new ArrayList<>();
        for(StopTime stopTime : stopTimeRepository.findAllById(ids)) {
            updates.add(stopTime.getUpdate());
        }

        return updates;
    }
}
