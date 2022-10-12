package com.translink.api.controller;

import com.translink.api.repository.RouteRepository;
import com.translink.api.repository.StopRepository;
import com.translink.api.repository.TripRepository;
import com.translink.api.repository.model.Route;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.Trip;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {
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
//        return StopDTO.mapFrom(stopRepository.findByStopId(id));
        return stopRepository.findByStopId(id);
    }

    @GetMapping("/trip")
    public Trip getTripDetail(String id) {
//        return TripDTO.mapFrom(tripRepository.findByTripId(id));
        return tripRepository.findByTripId(id);
    }

    @GetMapping("/route")
    public Route getRouteDetail(String id) {
//        return RouteDTO.mapFrom(routeRepository.findByRouteId(id));
        return routeRepository.findByRouteId(id);
    }

    @GetMapping("/stopRouteList")
    public Route[] getStopRouteDetail(String sid) {
        ArrayList<String> routeIDList = new ArrayList<>();
        for (int i = 0; i < stopRepository.findByStopId(sid).getStopTimes().size(); i++) {
            String temp = stopRepository.findByStopId(sid).getStopTimes().get(i).getTrip().getRoute().getRouteId();
            if (!(routeIDList.contains(temp))) {
                routeIDList.add(temp);
            }
        }

        ArrayList<Route> routeList = new ArrayList<>();
        for (String routes : routeIDList) {
            routeList.add(routeRepository.findByRouteId(routes));
        }

        Route[] routeArr = new Route[routeList.size()];
        routeArr = routeList.toArray(routeArr);
        return routeArr;
    }
}
