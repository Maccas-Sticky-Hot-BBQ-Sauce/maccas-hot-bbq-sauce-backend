package com.translink.api.config.format.converter;

import com.translink.api.config.format.model.SpecializedTime;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;

import java.util.Collections;

// Source: https://stackoverflow.com/questions/66991044/how-to-save-a-java-time-instant-in-mongodb-and-load-the-same-value-out-without-e
public class SpecializedTimeWriteConverter implements Converter<SpecializedTime, Document> {
    @Override
    public Document convert(SpecializedTime source) {
        return new Document(Collections.singletonMap("specializedTime", source.toString()));
    }
}
