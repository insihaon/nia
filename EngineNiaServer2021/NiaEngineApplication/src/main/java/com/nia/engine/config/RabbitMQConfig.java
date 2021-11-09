package com.nia.engine.config;

import com.nia.engine.listener.*;
import com.nia.engine.vo.RabbitMQVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);
	
	@Autowired
	private RabbitMQVo rabbitMQVo;
	
	@Autowired
	private EngineAlarmMsgListener engineAlarmMsgListener;

    @Autowired
    private UITicketMsgListener uiTicketMsgListener;

	@Autowired
	private EngineClearAlarmMsgListener engineClearAlarmMsgListener;

    @Autowired
    private PerformanceMsgListener performanceMsgListener;

//    @Autowired
//	private UITicketMsgListener uiTicketMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
    	 CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
         connectionFactory.setUsername(rabbitMQVo.getUserName());
         connectionFactory.setPassword(rabbitMQVo.getPassword());
         connectionFactory.setPort(rabbitMQVo.getPort());
         connectionFactory.setConnectionTimeout(5000);
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
    	return new Jackson2JsonMessageConverter();
    }
  
    @Bean(name="Engine_Queue")
    public Queue engineQueue() {
        return new Queue(rabbitMQVo.getEngineQueue());
    }
    
    @Bean(name="EngineClear_Queue")
    public Queue engineClearQueue() {
        return new Queue(rabbitMQVo.getEngineClearQueue());
    }

    @Bean(name="EngineToUiTicket_Queue")
    public Queue engineToUiTicketQueue() {
        return new Queue(rabbitMQVo.getEngineToUiQueue());
    }

    @Bean(name="PerformanceToEngineTicket_Queue")
    public Queue PerformanceToEngineTicketQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceEngineQueue());
    }

    @Bean(name="UIToEngineTicket_Queue")
    public Queue UIToEngineTicketQueue() { return new Queue(rabbitMQVo.getUIToEngineQueue()); }

    @Bean(name="AiIpTrafficResult_Queue")
    public Queue AiIpTrafficResultQueue() { return new Queue(rabbitMQVo.getAiIpTrafficResultQueue()); }

    @Bean(name="EngineToUiTicket_RabbitTemplate")
    public RabbitTemplate rabbitTemplateTicketEngineToUI() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getEngineToUiQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="UiToEngineTicket_RabbitTemplate")
    public RabbitTemplate rabbitTemplateTicketUiToEngine() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getUIToEngineQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    
    @Bean(name="Engine_ListenerContainer")
    public SimpleMessageListenerContainer engineListenerContainer() {
    	SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(engineQueue());
        listenerContainer.setMessageListener(engineAlarmMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }
    
    @Bean(name="EngineClear_ListenerContainer")
    public SimpleMessageListenerContainer engineClearListenerContainer() {
    	SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(engineClearQueue());
        listenerContainer.setMessageListener(engineClearAlarmMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="Performance_ListenerContainer")
    public SimpleMessageListenerContainer performanceListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(PerformanceToEngineTicketQueue());
        listenerContainer.setMessageListener(performanceMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="UIToEngine_ListenerContainer")
    public SimpleMessageListenerContainer UIToengineListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(UIToEngineTicketQueue());
        listenerContainer.setMessageListener(uiTicketMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }
}
