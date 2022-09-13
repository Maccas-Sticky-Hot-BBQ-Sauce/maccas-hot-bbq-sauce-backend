package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Stop {
    @Id
    @NotBlank
    private String stopId;

    private String stopCode;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private double latitude;

    @NotBlank
    private double longitude;

    private String zoneId;

    @URL
    private String stopUrl;

    @PositiveOrZero
    private int locationType;

    @DocumentReference
    @JsonBackReference
    @ToString.Exclude
    private List<StopTime> stopTimes;

    @DocumentReference(lazy = true)
    @JsonManagedReference
    private Stop parentStop;

    @DocumentReference
    @JsonBackReference
    @ToString.Exclude
    private List<Stop> childStops;

    private String platformCode;
}
