package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.translink.api.repository.model.embed.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Trip {
    @Id
    private String id;

    @Indexed
    @NotBlank
    private String tripId;

    @NotBlank
    private String serviceId;

    @NotBlank
    private String shapeId;

    @DocumentReference
    @ToString.Exclude
    @JsonIgnore
    private Route route;

    @NotBlank
    private String headsign;

    @NotNull
    private Direction direction;

    private String blockId;

    @DocumentReference
    @ToString.Exclude
    @JsonIgnore
    private List<StopTime> stopTimes;

    @NotEmpty
    @ToString.Exclude
    private List<Shape> shapes;

    @NotNull
    @ToString.Exclude
    private Calendar calendar;

    @NotNull
    @ToString.Exclude
    private List<CalendarException> exceptions;
}
