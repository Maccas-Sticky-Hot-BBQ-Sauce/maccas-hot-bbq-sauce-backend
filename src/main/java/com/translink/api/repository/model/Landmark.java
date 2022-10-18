package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Landmark {
    @Id
    private String id;

    @Indexed
    @NotBlank
    @JsonIgnore
    private String stopId;

    private double latitude;
    private double longitude;

    @NotBlank
    private String name;

    @NotNull
    private String image;
    private double distance;

    private double rating;

    private String icon;
// TODO:
//    private String url;
//
//    private String description;

    public Landmark copy() {
        return Landmark.builder()
                .id(this.id)
                .stopId(this.stopId)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .name(this.name)
                .image(this.image)
                .distance(this.distance)
                .rating(this.rating)
                .icon(this.icon)
                .build();
    }
}
