package com.codej.nia.mq.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import com.codej.nia.properties.NiaRabbitMQProperites;
import com.codej.mq.config.BaseRabbitMQConfiguration;

@Configuration
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class NiaRabbitMQConfiguration extends BaseRabbitMQConfiguration {

    @Autowired
    private NiaRabbitMQProperites rabbitMqProperites;

    @Bean(name = "NiaUiToEngine_RabbitTemplate")
    public RabbitTemplate niaUiToEngineTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMqProperites.getNiaUiToEngine());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
