package com.translink.api.config.format.converter;

import com.translink.api.config.format.model.SpecializedTime;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

// Source: https://stackoverflow.com/questions/66991044/how-to-save-a-java-time-instant-in-mongodb-and-load-the-same-value-out-without-e
public class SpecializedTimeReadConverter implements Converter<Document, SpecializedTime> {
    @Override
    public SpecializedTime convert(@NotNull Document source) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return SpecializedTime.parse(String.valueOf(source.get("specializedTime")), timeFormatter);
    }
}
