package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.translink.api.config.format.DepthSerializable;
import com.translink.api.repository.model.embed.Calendar;
import com.translink.api.repository.model.embed.CalendarException;
import com.translink.api.repository.model.embed.Direction;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Trip implements DepthSerializable {
    @Id
    private String id;

    @NotBlank
    private String serviceId;

    @NotBlank
    private String shapeId;

    @DocumentReference
    @Indexed
    @ToString.Exclude
    @JsonIgnore
    private Route route;

    @NotBlank
    private String headsign;

    @NotNull
    private Direction direction;

    private String blockId;

    @DocumentReference(lazy = true)
    @Indexed
    @ToString.Exclude
    @JsonIgnore
    private List<StopTime> stopTimes;

    @DocumentReference(lazy = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Shape> shapes;

    @NotNull
    @Indexed
    @ToString.Exclude
    private Calendar calendar;

    @NotNull
    @ToString.Exclude
    private List<CalendarException> exceptions;

    @Override
    public ObjectNode toJson(int depth, ObjectMapper mapper, Class<?> originalClass) {
        ObjectNode node = mapper.convertValue(this, ObjectNode.class);

        ObjectNode routeNode = route.toJson(depth-1, mapper, originalClass);
        node.set("route", routeNode);

        if(Trip.class.equals(originalClass)) {
            ArrayNode shapeNode = mapper.createArrayNode();
            shapes.stream()
                    .map(shape -> shape.toJson(depth-1, mapper, originalClass))
                    .forEach(shapeNode::add);

            node.set("shapes", shapeNode);
        }

        if(depth > 1) {
            ArrayNode stopTimesNode = mapper.createArrayNode();
            stopTimes.stream()
                    .map(stopTime -> stopTime.toJson(depth-1, mapper, originalClass))
                    .forEach(stopTimesNode::add);

            node.set("stopTimes", stopTimesNode);
        }

        return node;
    }
}
