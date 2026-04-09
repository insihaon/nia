//package com.nia.rca.test.simulator.config;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
//import org.springframework.scheduling.quartz.JobDetailFactoryBean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//
//@Configuration
//public class QuartzConfiguration implements ApplicationContextAware{
//
//	private static ApplicationContext context;
//
//	public static ApplicationContext getApplicationContext() {
//	    return context;
//	}
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//	    context = applicationContext;
//	}
//
//	@Bean
//    public JobDetailFactoryBean jobDetailFactoryBean(){
//        JobDetailFactoryBean factory = new JobDetailFactoryBean();
//        factory.setJobClass(AlarmSchedulerJobService.class);
//        factory.setGroup("alarmGroup");
//        factory.setName("alarmJob");
//        return factory;
//    }
//
//    @Bean
//    public CronTriggerFactoryBean cronTriggerFactoryBean(){
//        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
//        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
//        stFactory.setStartDelay(5000);
//        stFactory.setName("alarmTrigger");
//        stFactory.setGroup("alarmGroup");
//        stFactory.setCronExpression("* * * * * ?");
//        return stFactory;
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() {
//        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
//        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(context);
//        scheduler.setJobFactory(jobFactory);
//        scheduler.setTriggers(cronTriggerFactoryBean().getObject());
//        return scheduler;
//    }
//}
//
