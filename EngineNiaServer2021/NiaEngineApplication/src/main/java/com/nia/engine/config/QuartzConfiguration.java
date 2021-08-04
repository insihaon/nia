package com.nia.engine.config;

import com.nia.engine.service.impl.EngineSchedulerJobServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfiguration implements ApplicationContextAware{
	
	private static ApplicationContext context;

	public static ApplicationContext getApplicationContext() {
	    return context;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    context = applicationContext;
	}
	
	@Bean
	public JobDetailFactoryBean ticketDeleteJobDetailFactoryBean(){
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(EngineSchedulerJobServiceImpl.class);
		factory.setGroup("ticketDeleteGroup");
		factory.setName("ticketDeleteJob");
		return factory;
	}

	@Bean
	public CronTriggerFactoryBean ticketDeleteCronTriggerFactoryBean(){
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(ticketDeleteJobDetailFactoryBean().getObject());
		stFactory.setStartDelay(5000);
		stFactory.setName("ticketDeleteTrigger");
		stFactory.setGroup("ticketDeleteGroup");
		stFactory.setCronExpression("0 0 2 * * ?"); //매일 2시
		return stFactory;
	}

	@Bean
	public SchedulerFactoryBean ticketDeleteSchedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(context);
	    scheduler.setJobFactory(jobFactory);
		scheduler.setTriggers(ticketDeleteCronTriggerFactoryBean().getObject());
		return scheduler;
	}
}

