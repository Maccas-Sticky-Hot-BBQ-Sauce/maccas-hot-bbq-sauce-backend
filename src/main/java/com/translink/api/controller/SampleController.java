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

    /*
    @GetMapping("/stopTest")
    public List<StopTime> getStopDetailTest(String id) {
//        return StopDTO.mapFrom(stopRepository.findByStopId(id));
        return StopTimeRepository.findByStopId(id);
    }
    */

    
    /*
    @GetMapping("/stopPeriod")
    public Stop getStopPeriodDetail(String id, Integer tp) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        SpecializedTime startTime = SpecializedTime.parse("00:00:00", timeFormatter);
        SpecializedTime endTime = SpecializedTime.parse("05:59:59", timeFormatter);
        for (int i = 0; i < stopRepository.findByStopId(id).getStopTimes().size(); i++) {
            stopRepository.findByStopId(id).getStopTimes().get(0).getDeparture();
        }
        return stopRepository.findByStopId(id);
    }
    */
}
