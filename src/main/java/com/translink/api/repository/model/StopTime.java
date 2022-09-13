package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.Trip;
import com.translink.api.repository.model.embed.StopDropOffType;
import com.translink.api.repository.model.embed.StopPickupType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class StopTime {
    @Id
    private String id;

    @NotNull
    private SpecializedTime arrival;

    @NotNull
    private SpecializedTime departure;

    @NotNull
    @DocumentReference(lazy = true)
    @JsonManagedReference
    @ToString.Exclude
    private Stop stop;

    @NotNull
    @DocumentReference(lazy = true)
    @JsonManagedReference
    @ToString.Exclude
    private Trip trip;

    @PositiveOrZero
    private int sequence;

    @NotNull
    private StopPickupType pickupType;

    @NotNull
    private StopDropOffType dropOffType;
}
