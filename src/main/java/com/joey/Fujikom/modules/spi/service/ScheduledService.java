package com.joey.Fujikom.modules.spi.service;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class ScheduledService {
	
	private static Logger logger = Logger.getLogger(ScheduledService.class);

	@Scheduled(cron = "0 0 2 * * *")  
    void doSomethingWithDelay(){
    System.out.println("定时任务开始执行……");
    logger.info("serverPath==>");
    }
}
