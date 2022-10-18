package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.translink.api.config.format.DepthSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shape implements DepthSerializable {
    @Id
    @JsonIgnore
    private String id;

    private int sequence;
    private double latitude;
    private double longitude;

    @Override
    public ObjectNode toJson(int depth, ObjectMapper mapper, Class<?> originalClass) {
        return mapper.convertValue(this, ObjectNode.class);
    }
}
