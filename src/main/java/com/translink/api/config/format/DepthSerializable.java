package com.translink.api.config.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;

public interface DepthSerializable extends Serializable {
    ObjectNode toJson(int depth, ObjectMapper mapper, Class<?> originalClass);
}
