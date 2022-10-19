package com.translink.api.realtime.config;

import com.translink.api.realtime.TripUpdateJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Configuration
public class RealtimeConfig {
    @Value("${service.gtfs.scheduler.interval}")
    private int interval;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobDetail tripUpdateJobDetail() {
        return JobBuilder.newJob(TripUpdateJob.class)
                .requestRecovery(true)
                .withIdentity("Quartz_TripUpdate")
                .withDescription("Fetches Trip Updates from GTFS")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("Quartz_JobTrigger")
                .withDescription("Triggers every 5 minutes")
                .withSchedule(simpleSchedule().repeatForever().withIntervalInMinutes(interval))
                .build();
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }


    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springBeanJobFactory());
        factory.setQuartzProperties(quartzProperties());

        return factory;
    }
}
