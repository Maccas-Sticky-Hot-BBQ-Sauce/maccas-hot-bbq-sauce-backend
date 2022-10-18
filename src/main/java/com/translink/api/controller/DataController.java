package com.translink.api.controller;

import com.translink.api.repository.RouteRepository;
import com.translink.api.repository.StopRepository;
import com.translink.api.repository.TripRepository;
import com.translink.api.repository.model.Route;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/stop")
    public Stop getStopDetail(String id) {
        return stopRepository.findById(id).get();
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
