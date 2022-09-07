package com.translink.api.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Shape {
    private int sequence;

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;
}
