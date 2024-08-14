package com.codej.demo.mq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codej.demo.mq.handler.AmqpAlarmListener;
import com.codej.demo.mq.properties.DemoRabbitMQProperites;
import com.codej.mq.config.BaseRabbitMQConfiguration;

@Configuration
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class RabbitMQConfiguration extends BaseRabbitMQConfiguration {

    @Autowired
    private DemoRabbitMQProperites rabbitMqProperites;

    @Autowired(required = false)
    private AmqpAlarmListener amqpAlarmListener;

    @Bean(name = "UiToEngine_RabbitTemplate")
    public RabbitTemplate UiToEngineTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMqProperites.getUiToEngine());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name = "EngineToUi_Queue")
    public Queue engineToUiQueue() {
        return new Queue(rabbitMqProperites.getEngineToUi());
    }

    @Bean(name = "EngineToUi_ListenerContainer")
    public SimpleMessageListenerContainer topasProcessListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(engineToUiQueue());
        listenerContainer.setMessageListener(amqpAlarmListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

}
