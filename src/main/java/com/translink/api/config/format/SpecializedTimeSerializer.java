package com.translink.api.config.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.translink.api.config.format.model.SpecializedTime;

import java.io.IOException;

public class SpecializedTimeSerializer extends JsonSerializer<SpecializedTime> {
    @Override
    public void serialize(SpecializedTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());
    }
}
