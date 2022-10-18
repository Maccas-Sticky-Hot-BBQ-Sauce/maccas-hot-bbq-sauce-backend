package com.translink.api.config.format;

import com.translink.api.config.format.model.SpecializedTime;
import org.springframework.format.Formatter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SpecializedTimeFormatter implements Formatter<SpecializedTime> {
    @Override
    public SpecializedTime parse(String text, Locale locale) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return SpecializedTime.parse(text, timeFormatter);
    }

    @Override
    public String print(SpecializedTime object, Locale locale) {
        return object.toString();
    }
}
