package com.translink.api.staticdata.service;

import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.translink.api.config.advice.annotation.Delayed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
public class PlacesService {
    @Value("${service.google.maps.options.radius}")
    private int radius;

    private GeoApiContext context;

    @Autowired
    public void setContext(GeoApiContext context) {
        this.context = context;
    }

    public List<PlacesSearchResponse> findNearbyPlaces(LatLng location) throws InterruptedException, IOException, ApiException {
        PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, location)
                .radius(1000)
                .rankby(RankBy.PROMINENCE)
                .type(PlaceType.TOURIST_ATTRACTION)
                .await();

        List<PlacesSearchResponse> list = new ArrayList<>();
        list.add(response);

        while(response.nextPageToken != null) {
            response = findNextPage(response.nextPageToken);
            list.add(response);
        }

        return list;
    }

//    @Delayed(3000)
    public PlacesSearchResponse findNextPage(String nextPageToken) throws IOException, InterruptedException, ApiException {
        Thread.sleep(2500);
        return PlacesApi.nearbySearchNextPage(context, nextPageToken).await();
    }

    public String getPhoto(String photoReference, int maxHeight, int maxWidth) throws IOException, InterruptedException, ApiException {
        ImageResult result = PlacesApi.photo(context, photoReference)
                .maxHeight(maxHeight)
                .maxWidth(maxWidth)
                .await();

        return new String(Base64.getEncoder().encode(result.imageData));
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.round(Math.sqrt(distance));
    }
}