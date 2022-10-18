package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.translink.api.config.format.DepthSerializable;
import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.model.embed.StopDropOffType;
import com.translink.api.repository.model.embed.StopPickupType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class StopTime implements DepthSerializable {
    @Id
    private String id;

    @NotNull
    @Indexed
    private SpecializedTime arrival;

    @NotNull
    @Indexed
    private SpecializedTime departure;

    @Indexed
    @NotBlank
    private String stopId;

    @DocumentReference(lookup = "{ '_id': ?#{#self.stopId} }")
    @ReadOnlyProperty
    @ToString.Exclude
    @JsonIgnore
    private Stop stop;

    @Indexed
    @NotBlank
    private String tripId;

    @DocumentReference(lookup = "{ '_id': ?#{#self.tripId} }")
    @ReadOnlyProperty
    @ToString.Exclude
    @JsonIgnore
    private Trip trip;

    @PositiveOrZero
    private int sequence;

    @NotNull
    private StopPickupType pickupType;

    @NotNull
    private StopDropOffType dropOffType;

    @Override
    public ObjectNode toJson(int depth, ObjectMapper mapper, Class<?> originalClass) {
        ObjectNode node = mapper.convertValue(this, ObjectNode.class);

        if(depth > 1) {
            if(!Trip.class.equals(originalClass)) {
                ObjectNode tripNode = trip.toJson(depth-1, mapper, originalClass);
                node.set("trip", tripNode);
            }

            if(!Stop.class.equals(originalClass)) {
                ObjectNode stopNode = stop.toJson(depth-1, mapper, originalClass);
                node.set("stop", stopNode);
            }
        }

        return node;
    }
}
