package com.nia.data.linkage.ipsdn.config;

import com.nia.data.linkage.ipsdn.vo.config.RabbitMQVo;
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

    @Bean(name="NiaSflowJsonFileKey_Queue")
    public Queue niaSyslogAlarmQueue() {
        return new Queue(rabbitMQVo.getNiaSflowJsonFileKeyDirectly());
    }


    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaSflowJsonFileKey_RabbitTemplate")
    public RabbitTemplate rabbitTemplateCluster() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaSflowJsonFileKeyDirectly());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
