package com.translink.api.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.maps.GeoApiContext;
import com.translink.api.config.format.SpecializedTimeFormatter;
import com.translink.api.config.format.SpecializedTimeSerializer;
import com.translink.api.config.format.converter.SpecializedTimeReadConverter;
import com.translink.api.config.format.converter.SpecializedTimeWriteConverter;
import com.translink.api.config.format.model.SpecializedTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAspectJAutoProxy
@EnableAsync
public class AppConfig implements WebMvcConfigurer {
    @Value("${service.google.maps.key}")
    private String apiKey;

    @Value("${service.google.limit}")
    private int queryRateLimit;

    @Value("${service.google.retry}")
    private int maxRetries;

    @Value("${service.google.timeout}")
    private int maxTimeout;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new SpecializedTimeFormatter());
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new SpecializedTimeWriteConverter());
        converters.add(new SpecializedTimeReadConverter());

        return new MongoCustomConversions(converters);
    }

    @Bean
    public SimpleModule specializedTimeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(SpecializedTime.class, new SpecializedTimeSerializer());

        return simpleModule;
    }

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(apiKey)
                .queryRateLimit(queryRateLimit)
                .maxRetries(maxRetries)
                .connectTimeout(maxTimeout, TimeUnit.SECONDS)
                .readTimeout(maxTimeout, TimeUnit.SECONDS)
                .connectTimeout(maxTimeout, TimeUnit.SECONDS)
                .writeTimeout(maxTimeout, TimeUnit.SECONDS)
                .build();
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
