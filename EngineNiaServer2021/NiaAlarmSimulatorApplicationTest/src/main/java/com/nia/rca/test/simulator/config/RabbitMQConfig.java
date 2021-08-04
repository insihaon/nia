package com.nia.rca.test.simulator.config;

import com.nia.rca.test.simulator.listener.RcaResetMsgListener;
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

import com.nia.rca.test.simulator.vo.RabbitMQVo;

@Configuration
public class RabbitMQConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);
	
	@Autowired
	private RabbitMQVo rabbitMQVo;

	@Autowired
    private RcaResetMsgListener rcaResetMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        connectionFactory.setConnectionTimeout(5000);
        return connectionFactory;
    }

    @Bean(name="NiaAlarm_Queue")
    public Queue niaAlarmQueue() {
        return new Queue(rabbitMQVo.getNiaAlarmQueue());
    }

    @Bean(name="NiaTicketReStart_Queue")
    public Queue niaTicketReStartQueue() {
        return new Queue(rabbitMQVo.getNiaTicketReStartQueue());
    }

    @Bean(name="NiaPerformance_Queue")
    public Queue getNiaPerformanceQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceQueue());
    }

    @Bean(name="PerformanceToAiSend_Queue")
    public Queue niaPerformanceToAiSendQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceToAiSendQueue());
    }

    @Bean(name="uiToEngine_Queue")
    public Queue uiToEngine_Queue() {
        return new Queue(rabbitMQVo.getUiToEngineQueue());
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaAlarm_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaAlarm() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaAlarmQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="NiaPerformance_RabbitTemplate")
    public RabbitTemplate rabbitTemplateMbaApToUi() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaPerformanceQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="NiaTicketReStart_ListenerContainer")
    public SimpleMessageListenerContainer niaTicketReStartListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(niaTicketReStartQueue());
        listenerContainer.setMessageListener(rcaResetMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="PerformanceToAiSend_RabbitTemplate")
    public RabbitTemplate rabbitTemplatePerformanceToAiSend() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaPerformanceToAiSendQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="UiToEngine_RabbitTemplate")
    public RabbitTemplate rabbitTemplateUiToEngine() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getUiToEngineQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
