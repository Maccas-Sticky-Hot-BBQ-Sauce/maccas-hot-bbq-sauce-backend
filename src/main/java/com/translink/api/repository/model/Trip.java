package com.translink.api.repository.model;

import com.translink.api.repository.model.embed.Direction;
import com.translink.api.repository.model.embed.Shape;
import com.translink.api.repository.model.embed.StopTimes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Document
public class Trip {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    private String tripId;

    @NotBlank
    private String headsign;

    @NotNull
    private Direction direction;

    private String blockId;

    @NotNull
    private List<StopTimes> stopTimes;

    @NotBlank
    private String shapeId;

    @NotNull
    private List<Shape> shapes;
}
