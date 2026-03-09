package com.nia.korenrca.server.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import io.vertx.core.json.JsonObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CronTriggerRunner {
    public static Object defaultChartData = new JsonObject();
    public CronTriggerRunner(){
        QuartzJob quartzJob = new QuartzJob();
        CronTriggerRunner.defaultChartData = quartzJob.doChartRequest();
    }
    public void task() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail job = newJob(QuartzJob.class)
                .withIdentity("jobName", Scheduler.DEFAULT_GROUP)
                .build();

            Trigger trigger = newTrigger()
                .withIdentity("trggerName", Scheduler.DEFAULT_GROUP)
                .withSchedule(cronSchedule("0 0 0/1 * * ?"))
                .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
