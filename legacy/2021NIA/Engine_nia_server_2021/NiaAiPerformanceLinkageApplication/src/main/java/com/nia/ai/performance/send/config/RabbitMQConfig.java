package com.nia.ai.performance.send.config;

import com.nia.ai.performance.send.listener.NiaAiPerformanceListener;
import com.nia.ai.performance.send.vo.config.RabbitMQVo;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Autowired
	private RabbitMQVo rabbitMQVo;

	@Autowired
    private NiaAiPerformanceListener niaAiPerformanceListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="PerformanceToAiSend_Queue")
    public Queue niaPerformanceToAiSendQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceToAiSendQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="PerformanceToAiSend_RabbitTemplate")
    public RabbitTemplate rabbitTemplateTopasLinkage() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaPerformanceToAiSendQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="PerformanceToAiSend_ListenerContainer")
    public SimpleMessageListenerContainer niaPerformanceToAiSendListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(niaPerformanceToAiSendQueue());
        listenerContainer.setMessageListener(niaAiPerformanceListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }
}
