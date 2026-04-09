package com.nia.ai.per.ap.config;

import com.nia.ai.per.ap.listener.PerformanceMsgListener;
import com.nia.ai.per.ap.vo.RabbitMQVo;
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
public class RabbitMQConfig{

	@Autowired
	private RabbitMQVo rabbitMQVo;

    @Autowired
    private PerformanceMsgListener performanceMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="NiaPerformance_Queue")
    public Queue getNiaPerformanceQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceQueue());
    }

    @Bean(name="NiaPerformanceEngine_Queue")
    public Queue getNiaPerformanceEngineQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceEngineQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaPerformanceEngine_RabbitTemplate")
    public RabbitTemplate rabbitTemplatePerformanceToEngine() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaPerformanceEngineQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="performance_ListenerContainer")
    public SimpleMessageListenerContainer mbaPerformancesListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(getNiaPerformanceQueue());
        listenerContainer.setMessageListener(performanceMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

}
