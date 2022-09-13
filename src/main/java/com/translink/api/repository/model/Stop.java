package com.translink.api.repository.model;

import com.translink.api.repository.model.embed.StopTimes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@Document
public class Stop {
    @Id
    private String id;

    @Indexed
    @NotBlank
    private String stopId;

    @Indexed
    @NotBlank
    private String stopCode;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private Double latitude;

    @NotBlank
    private Double longitude;

    private int zoneId;

    @NotBlank
    @URL
    private String stopUrl;

    @PositiveOrZero
    private int locationType;

    @DocumentReference(lazy = true)
    private List<StopTimes> stopTimes;

    @DocumentReference
    private Stop parentStop;

    @DocumentReference(lazy = true)
    private List<Stop> childStops;

    private String platformCode;
}
