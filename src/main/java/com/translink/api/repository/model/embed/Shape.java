package com.translink.api.repository.model.embed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shape {
    private int sequence;
    private double latitude;
    private double longitude;
}
