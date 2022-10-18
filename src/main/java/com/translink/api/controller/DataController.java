package com.translink.api.controller;

import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.RouteRepository;
import com.translink.api.repository.StopRepository;
import com.translink.api.repository.TripRepository;
import com.translink.api.repository.model.Route;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.Trip;
import com.translink.api.repository.model.embed.Days;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class DataController {
    private TripRepository tripRepository;
    private StopRepository stopRepository;
    private RouteRepository routeRepository;

    @Autowired
    public void setStopRepository(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Autowired
    public void setRouteRepository(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @GetMapping(value = "/stop", params = {"id"})
    public Stop getStopDetail(@RequestParam String id) {
        Days days = Days.valueOf(LocalDate.now().getDayOfWeek().name().toUpperCase());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        SpecializedTime fromTime = SpecializedTime.parse("00:00:00", timeFormatter);
        SpecializedTime toTime = SpecializedTime.parse("30:59:59", timeFormatter);

        return stopRepository.findStopByStopIdFilterByDayAndTime(id, days, fromTime, toTime);
    }

    @GetMapping(value = "/stop/filter", params = {"id", "fromTime", "toTime"})
    public Stop getStopByDepartureTime(@RequestParam String id,
                                       @RequestParam SpecializedTime fromTime,
                                       @RequestParam SpecializedTime toTime) {
        Days days = Days.valueOf(LocalDate.now().getDayOfWeek().name().toUpperCase());

        return stopRepository.findStopByStopIdFilterByDayAndTime(id, days, fromTime, toTime);
    }

    @GetMapping("/trip")
    public Trip getTripDetail(String id) {
        return tripRepository.findById(id).get();
    }

    @GetMapping("/route")
    public Route getRouteDetail(String id) {
        return routeRepository.findById(id).get();
    }
}
