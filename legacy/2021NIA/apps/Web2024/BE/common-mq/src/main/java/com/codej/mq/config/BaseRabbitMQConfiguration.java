package com.codej.mq.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codej.mq.properties.RabbitMQProperites;


@Configuration
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class BaseRabbitMQConfiguration {

    @Autowired
    private RabbitMQProperites rabbitMqProperites;

    @Bean
    public ConnectionFactory connectionFactory() {
        String address = rabbitMqProperites.getEmbeddedEnabled() ? "127.0.0.1" : rabbitMqProperites.getAddress();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(address);
        connectionFactory.setUsername(rabbitMqProperites.getUserName());
        connectionFactory.setPassword(rabbitMqProperites.getPassword());
        connectionFactory.setPort(rabbitMqProperites.getPort());
        return connectionFactory;
    }

    @Bean(name = "JsonMessageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
