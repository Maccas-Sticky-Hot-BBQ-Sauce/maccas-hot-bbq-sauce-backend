package com.translink.api.repository.model.embed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Calendar {
    @NotEmpty
    private Set<Days> days;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
