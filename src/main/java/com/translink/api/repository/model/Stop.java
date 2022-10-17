package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.translink.api.config.format.DepthSerializable;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class Stop implements DepthSerializable {
    @Id
    private String id;

    @Indexed
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

    @DocumentReference(lazy = true)
    @ToString.Exclude
    @JsonIgnore
    private List<StopTime> stopTimes;

    @DocumentReference(lazy = true)
    @JsonIgnore
    private Stop parentStop;

    @DocumentReference
    @JsonIgnore
    @ToString.Exclude
    private List<Stop> childStops;

    private String platformCode;

    @Override
    public ObjectNode toJson(int depth, ObjectMapper mapper, Class<?> originalClass) {
        ObjectNode node = mapper.convertValue(this, ObjectNode.class);

        if(depth > 1) {
            ArrayNode stopTimesNode = mapper.createArrayNode();
            stopTimes.stream()
                    .map(stopTime -> stopTime.toJson(depth-1, mapper, originalClass))
                    .forEach(stopTimesNode::add);

            node.set("stopTimes", stopTimesNode);

            if(Stop.class.equals(originalClass)) {
                if(parentStop != null && parentStop.getId() != null) {
                    ObjectNode parentNode = parentStop.toJson(depth-1, mapper, originalClass);
                    node.set("parentStop", parentNode);
                }

                if(childStops != null && !childStops.isEmpty()) {
                    ArrayNode childNode = mapper.createArrayNode();
                    childStops.stream()
                            .map(stop -> stop.toJson(depth, mapper, originalClass))
                            .forEach(childNode::add);

                    node.set("childStop", childNode);
                }
            }
        }

        return node;
    }
}
