package com.translink.api.repository.model.embed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.translink.api.config.format.DepthSerializable;
import com.translink.api.config.format.model.SpecializedTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripUpdate implements DepthSerializable {
    private String stopTimeId;
    private ExpectedTime arrival;
    private ExpectedTime departure;
    private TripRelationship tripRelationship;
    private ScheduleRelationship scheduleRelationship;

    @Override
    public ObjectNode toJson(int depth, ObjectMapper mapper, Class<?> originalClass) {
        return mapper.convertValue(this, ObjectNode.class);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExpectedTime {
        private int delay;
        private SpecializedTime time;
        private int uncertainty;
    }

    public enum TripRelationship {
        SCHEDULED, ADDED, UNSCHEDULED, CANCELED
    }

    public enum ScheduleRelationship {
        SCHEDULED, SKIPPED, NO_DATA
    }
}
