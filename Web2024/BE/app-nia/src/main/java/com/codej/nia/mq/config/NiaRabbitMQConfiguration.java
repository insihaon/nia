package com.codej.nia.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codej.mq.config.BaseRabbitMQConfiguration;
import com.codej.nia.mq.handler.AmqpNiaAlarmListener;
import com.codej.nia.properties.NiaRabbitMQProperites;

@Configuration
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class NiaRabbitMQConfiguration extends BaseRabbitMQConfiguration {

    @Autowired
    private NiaRabbitMQProperites rabbitMqProperites;

    @Autowired(required = false)
    private AmqpNiaAlarmListener amqpNiaAlarmListener;

    // Engine-Queue 바이딩 사용

    // private static final String EXCHANGE_NAME = "NiaUiToEngine_exchange";
    // private static final String QUEUE_NAME = "NiaUiToEngine_queue";

    // // Create the exchange bean
    // @Bean
    // public Exchange niaUiToEngineExchange() {
    // return new DirectExchange(EXCHANGE_NAME);
    // }

    // // Create the queue bean
    // @Bean(name = "NiaUiToEngine_Queue")
    // public Queue niaUiToEngineQueue() {
    // return new Queue(QUEUE_NAME);
    // }

    // // Binding for exchange-queue
    // @Bean
    // public Binding binding(@Qualifier("NiaUiToEngine_Queue") Queue queue,
    // Exchange exchange) {
    // return BindingBuilder.bind(queue)
    // .to(exchange)
    // .with(rabbitMqProperites.getNiaUiToEngine())
    // .noargs(); // Add this line to resolve the type mismatch
    // }

    @Bean(name = "NiaUiToEngine_RabbitTemplate")
    public RabbitTemplate niaUiToEngineTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMqProperites.getNiaUiToEngine());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name = "NiaEngineToUi_Queue")
    public Queue niaEngineToUiQueue() {
        return new Queue(rabbitMqProperites.getNiaEngineToUi());
    }

    @Bean(name = "NiaEngineToUi_ListenerContainer")
    public SimpleMessageListenerContainer topasProcessListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(niaEngineToUiQueue());
        listenerContainer.setMessageListener(amqpNiaAlarmListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

}
