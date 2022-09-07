package com.translink.api.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Document
public class Route {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    private String routeId;

    @NotBlank
    private String shortName;

    @NotBlank
    private String longName;

    private String description;

    @NotNull
    private RouteType routeType;

    @NotBlank
    @URL
    private String routeUrl;

    @NotNull
    private String routeColor;

    @NotNull
    private String textColor;

    @DBRef
    private List<Trip> trips;
}
