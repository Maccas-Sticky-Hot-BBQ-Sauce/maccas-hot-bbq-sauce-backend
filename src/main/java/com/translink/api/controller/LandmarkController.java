package com.translink.api.controller;

import com.translink.api.repository.LandmarkRepository;
import com.translink.api.repository.model.Landmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/landmark")
public class LandmarkController {
    private LandmarkRepository landmarkRepository;

    @Autowired
    public void setLandmarkRepository(LandmarkRepository landmarkRepository) {
        this.landmarkRepository = landmarkRepository;
    }

    @GetMapping(params = {"stopId"})
    public List<Landmark> getLandmarkByStopId(@RequestParam String stopId) {
        return landmarkRepository.findByStopIdOrderByRatingDescLatitude(stopId, Pageable.ofSize(5)).toList();
    }
}
