package com.smart.example1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author fy
 * @version 1.0
 * @date 2018/7/31
 */
public class HelloJob implements Job {

    private Logger logger=LoggerFactory.getLogger(HelloJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Hello World!"+new Date());
    }
}
