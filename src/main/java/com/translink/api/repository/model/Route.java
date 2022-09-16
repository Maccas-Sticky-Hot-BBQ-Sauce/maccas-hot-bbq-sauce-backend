package com.translink.api.repository.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.translink.api.repository.model.embed.RouteType;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
public class Route {
    @Id
    private String id;

    @Indexed
    @NotBlank
    private String routeId;

    @NotBlank
    private String shortName;

    @NotBlank
    private String longName;

    @ToString.Exclude
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

    @DocumentReference(lazy = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Trip> trips;
}
