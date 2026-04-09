package com.nia.ems.linkage.config;

import com.nia.ems.linkage.listener.PerformanceFailListener;
import com.nia.ems.linkage.vo.config.RabbitMQVo;
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
    private PerformanceFailListener performanceFailListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="AlarmLinkageSend_Queue")
    public Queue alarmLinkageSendQueue() {
        return new Queue(rabbitMQVo.getAlarmLinkageResultQueue());
    }

    @Bean(name="PerformanceToAiSend_Queue")
    public Queue niaPerformanceToAiSendQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceToAiSendQueue());
    }

    @Bean(name="PerformanceFail_Queue")
    public Queue niaPerformanceFailQueue() {
        return new Queue(rabbitMQVo.getNiaPerformanceFailQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    @Bean(name="PerformanceFail_ListenerContainer")
    public SimpleMessageListenerContainer niaPerformanceFailListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(niaPerformanceFailQueue());
        listenerContainer.setMessageListener(performanceFailListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="AlarmLinkageResult_RabbitTemplate")
    public RabbitTemplate rabbitTemplateAlarmLinkage() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getAlarmLinkageResultQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="PerformanceToAiSend_RabbitTemplate")
    public RabbitTemplate rabbitTemplatePerformanceToAiSend() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaPerformanceToAiSendQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="PerformanceFail_RabbitTemplate")
    public RabbitTemplate rabbitTemplatePerformanceFail() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaPerformanceFailQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="MbaAiModel_Queue")
    public Queue mbaAiModelQueue() {
        return new Queue(rabbitMQVo.getMbaAiModelQueue());
    }

    @Bean(name="MbaAiModel_RabbitTemplate")
    public RabbitTemplate rabbitTemplateMbaAiModel() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getMbaAiModelQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
