package com.smart.example1;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author fy
 * @version 1.0
 * @date 2018/7/31
 */
public class SimpleExample {

    private Logger logger=LoggerFactory.getLogger(SimpleExample.class);

    private void run() throws Exception{
        logger.info("------- Initializing ----------------------");
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler=schedulerFactory.getScheduler();
        logger.info("------- Initialization Complete -----------");
        //当前时间四舍五入
        Date runTime=DateBuilder.evenMinuteDate(new Date());
        JobDetail jobDetail=JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
        Trigger trigger=TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
        scheduler.scheduleJob(jobDetail, trigger);
        logger.info(trigger.getKey()+" will at "+runTime);
        scheduler.start();
        logger.info("------- Started Scheduler -----------------");
        logger.info("------- Waiting 65 seconds... -------------");
        Thread.sleep(65*1000);
        logger.info("------- Shutting Down ---------------------");
        scheduler.shutdown();
        logger.info("------- Shutdown Complete -----------------");
    }

    public static void main(String[] args) throws Exception{
        new SimpleExample().run();
    }
}
