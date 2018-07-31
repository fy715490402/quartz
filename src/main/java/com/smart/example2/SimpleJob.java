package com.smart.example2;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * @author Administrator
 * @version 1.0
 * @date 2018-07-31
 */

public class SimpleJob implements Job {

    private Logger logger= LoggerFactory.getLogger(getClass());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey=jobExecutionContext.getJobDetail().getKey();
        logger.info("SimpleJob says: "+jobKey+" executing at "+new Date());
    }
}
