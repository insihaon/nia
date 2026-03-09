package com.nia.alarm.simulator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nia.alarm.simulator.vo.RabbitMQVo;

@Configuration
public class RabbitMQConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);
	
	@Autowired
	private RabbitMQVo rabbitMQVo;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        connectionFactory.setConnectionTimeout(5000);
        return connectionFactory;
    }

    @Bean(name="NiaAlarm_Queue")
    public Queue niaAlarmQueue() {
        return new Queue(rabbitMQVo.getNiaAlarmQueue());
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaAlarm_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaAlarm() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaAlarmQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
