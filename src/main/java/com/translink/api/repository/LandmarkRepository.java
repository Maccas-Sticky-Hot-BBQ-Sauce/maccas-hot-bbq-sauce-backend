package com.translink.api.repository;

import com.translink.api.repository.model.Landmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandmarkRepository extends MongoRepository<Landmark, String> {
    Page<Landmark> findByStopIdOrderByRatingDescLatitude(String stopId, Pageable pageable);
}
