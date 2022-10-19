package com.translink.api.realtime;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuartzInitializer {
    @Value("${execute-scheduler}")
    private boolean isExecuted;

    private Trigger trigger;
    private JobDetail job;
    private SchedulerFactoryBean factory;

    @Autowired
    public void setJob(JobDetail job) {
        this.job = job;
    }

    @Autowired
    public void setFactory(SchedulerFactoryBean factory) {
        this.factory = factory;
    }

    @Autowired
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Order(3)
    public void startQuartzScheduler() throws SchedulerException {
        if(isExecuted) {
            Scheduler scheduler = factory.getScheduler();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();

            log.info("Scheduler is started, next fire time: {}", trigger.getNextFireTime());
        }
    }
}
