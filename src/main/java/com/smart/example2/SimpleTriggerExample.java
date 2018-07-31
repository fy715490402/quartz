package com.smart.example2;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @date 2018-07-31
 */

public class SimpleTriggerExample {

    private void run() throws Exception{
        
        Logger logger= LoggerFactory.getLogger(getClass());
        logger.info("------- Initializing -------------------");
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler=schedulerFactory.getScheduler();
        logger.info("------- Initialization Complete --------");
        /*
            返回一个四舍五入到下一个偶数倍的日期秒数
            算法：当前时间13：58：10，初始值为13：58：00，加上时间间隔15，返回离初始时间最近的
         */
        Date startTime= DateBuilder.nextGivenSecondDate(null,15);
        JobDetail jobDetail= JobBuilder.newJob(SimpleJob.class).withIdentity("job1","group1").build();
        SimpleTrigger trigger= (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startAt(startTime).build();
        Date date=scheduler.scheduleJob(jobDetail,trigger);
        //job1 执行一次
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        //job2
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job2","group1").build();
        trigger= (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger2","group1").startAt(startTime).build();
        date=scheduler.scheduleJob(jobDetail,trigger);
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        //job3
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job3","group1").build();
        /*
             withSchedule(ScheduleBuilder<SBT> schedBuilder):设置用于定义Trigger的TriggerBuilder的时间表
             用于设置由TriggerBuilder创建的Trigger被触发的时间规则，如果需要在给定的时间内执行N次，入参是SimpleScheduleBuilder，如果是按照日历执行，入参是CronScheduleBuilder
         */
        //执行11次，运行一次，重复10次
        trigger=TriggerBuilder.newTrigger().withIdentity("trigger3","group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();
        date=scheduler.scheduleJob(jobDetail,trigger);
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        //job4
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job4","group1").build();
        //执行6次，运行一次，重复5次
        trigger=TriggerBuilder.newTrigger().withIdentity("trigger4","group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();
        date=scheduler.scheduleJob(jobDetail,trigger);
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        //job5
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job5","group1").build();
        /*
            futureDate(int interval,DateBuilder.IntervalUnit unit)：未来的某个时间执行，interval：间隔的时间值，unit：时间单位
         */
        //示例为5分钟后执行
        trigger= (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger5","group5").startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE)).build();
        date=scheduler.scheduleJob(jobDetail,trigger);
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        //job6
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job6","group1").build();
        trigger=TriggerBuilder.newTrigger().withIdentity("trigger6","group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(40)).build();
        date=scheduler.scheduleJob(jobDetail,trigger);
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        logger.info("------- Starting Scheduler ----------------");
        scheduler.start();
        logger.info("------- Started Scheduler -----------------");
        /*
            scheduler.start()后也可以向scheduler中添加作业
         */
        //job7
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job7","group1").build();
        //执行6次，运行一次，重复5次
        trigger=TriggerBuilder.newTrigger().withIdentity("trigger7","group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();
        date=scheduler.scheduleJob(jobDetail,trigger);
        logger.info(jobDetail.getKey()+" will run at "+date+" and repeat: "+trigger.getRepeatCount()+" times,every " +
                trigger.getRepeatInterval()/1000+" seconds");
        //job8
        //不需要trigger触发，job可以被执行;没有与触发器关联的job会是休眠状态，直到被触发器调度或者调用 Scheduler.triggerJob() 
        jobDetail=JobBuilder.newJob(SimpleJob.class).withIdentity("job8","group1").storeDurably().build();
        scheduler.addJob(jobDetail,true);
        logger.info("'Manually' triggering job8...");
        scheduler.triggerJob(JobKey.jobKey("job8","group1"));
        logger.info("------- Waiting 30 seconds... --------------");
        Thread.sleep(30000);
        logger.info("------- Rescheduling... --------------------");
        trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20))
                .build();

        date = scheduler.rescheduleJob(trigger.getKey(), trigger);
        logger.info("job7 rescheduled to run at: " + date);
        logger.info("------- Waiting five minutes... ------------");
        Thread.sleep(300000);
        logger.info("------- Shutting Down ---------------------");
        scheduler.shutdown(true);
        logger.info("------- Shutdown Complete -----------------");
    }

    public static void main(String[] args) throws Exception{
        new SimpleTriggerExample().run();
    }

}
