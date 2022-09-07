package com.translink.api.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class Calendar {
    @NotNull
    private String serviceId;

    @NotEmpty
    private Set<Days> days;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
