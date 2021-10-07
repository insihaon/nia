package com.nia.alarm.ip.simulator.config;

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

import com.nia.alarm.ip.simulator.vo.RabbitMQVo;

@Configuration
public class RabbitMQConfig {

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

    @Bean(name="NiaIpAlarm_Queue")
    public Queue niaIpAlarmQueue() {
        return new Queue(rabbitMQVo.getNiaIpAlarmQueue());
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaIpAlarm_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaIpAlarm() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaIpAlarmQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
