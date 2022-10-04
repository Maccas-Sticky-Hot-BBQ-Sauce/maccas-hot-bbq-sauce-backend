package com.translink.api.config.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class JacksonDepthSerializer extends BeanSerializer {
    public static final String DEPTH_KEY = "maxDepthSize";

    public JacksonDepthSerializer(BeanSerializerBase src) {
        super(src);
    }

    @Override
    protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        AtomicInteger depth = (AtomicInteger) provider.getAttribute(DEPTH_KEY);
        if (depth.decrementAndGet() >= 0) {
            super.serializeFields(bean, gen, provider);
        }
    }
}
