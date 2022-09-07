package com.translink.api.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CalendarDates {
    private LocalDate date;
    private ExceptionType exceptionType;
}
