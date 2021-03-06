package ua.alexch.currency.service.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class DownloadTaskScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTaskScheduler.class);

    private final ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public DownloadTaskScheduler(ThreadPoolTaskScheduler scheduler) {
        this.taskScheduler = scheduler;
    }

    public boolean scheduleCronTask(DownloadTask task, String expression) {
        try {
            CronTrigger trigger = new CronTrigger(expression);
            return taskScheduler.schedule(task, trigger) != null;

        } catch (Exception ex) {
            LOGGER.warn("Failed to schedule Task: {}", ex.toString());
        }
        return false;
    }
}
