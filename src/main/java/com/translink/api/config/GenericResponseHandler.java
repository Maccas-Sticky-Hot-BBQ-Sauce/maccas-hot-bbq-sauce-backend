package com.translink.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translink.api.config.format.DepthSerializable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GenericResponseHandler implements ResponseBodyAdvice {
    @Value("${spring.jackson.max-depth}")
    private int maxDepth;
    private ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !converterType.equals(StringHttpMessageConverter.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Pattern pattern = Pattern.compile("com\\.translink\\.api.*", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(returnType.getDeclaringClass().getName());
        if(!matcher.matches()) {
            return body;
        }

        if(body instanceof DepthSerializable) {
            return ((DepthSerializable) body).toJson(maxDepth, mapper);
        }

        return body;
    }
}
