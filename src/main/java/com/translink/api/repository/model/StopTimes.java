package com.translink.api.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class StopTimes {
    @NotNull
    private LocalTime arrival;

    @NotNull
    private LocalTime departure;

    @NotNull
    @DocumentReference
    private Stop stop;

    @PositiveOrZero
    private int sequence;

    @NotNull
    private StopPickupType pickupType;

    @NotNull
    private StopDropOffType dropOffType;
}
