package com.nia.data.linkage.config;

import com.nia.data.linkage.vo.config.RabbitMQVo;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return connectionFactory;
    }

    @Bean(name="AlarmLinkageSend_Queue")
    public Queue topasLinkageSendQueue() {
        return new Queue(rabbitMQVo.getAlarmLinkageResultQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="AlarmLinkageResult_RabbitTemplate")
    public RabbitTemplate rabbitTemplateTopasLinkage() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getAlarmLinkageResultQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
