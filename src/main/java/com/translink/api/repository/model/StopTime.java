package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.translink.api.config.format.DepthSerializable;
import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.Trip;
import com.translink.api.repository.model.embed.StopDropOffType;
import com.translink.api.repository.model.embed.StopPickupType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
    private SpecializedTime arrival;

    @NotNull
    private SpecializedTime departure;

    @DocumentReference(lazy = true)
    @ToString.Exclude
    @JsonIgnore
    private Stop stop;

    @DocumentReference(lazy = true)
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
    public ObjectNode toJson(int depth, ObjectMapper mapper) {
        ObjectNode node = mapper.convertValue(this, ObjectNode.class);

        if (depth > 1) {
            ObjectNode tripNode = trip.toJson(depth-1, mapper);
            node.set("trip", tripNode);

            ObjectNode stopNode = stop.toJson(depth-1, mapper);
            node.set("stop", stopNode);
        }

        return node;
    }
}
