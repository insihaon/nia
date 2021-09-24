package com.nia.alarm.ip.preprocessor.config;

import com.nia.alarm.ip.preprocessor.listener.NiaAlarmMsgListener;
import com.nia.alarm.ip.preprocessor.vo.config.RabbitMQVo;
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
    private NiaAlarmMsgListener niaAlarmMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="NiaIpAlarm_Queue")
    public Queue niaIpAlarmQueue() {
        return new Queue(rabbitMQVo.getNiaIpAlarmQueue());
    }

    @Bean(name="Cluster_Queue")
    public Queue engineQueue() {
        return new Queue(rabbitMQVo.getClusterQueue());
    }

    @Bean(name="EngineClear_Queue")
    public Queue engineClearQueue() {
        return new Queue(rabbitMQVo.getEngineClearQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="Cluster_RabbitTemplate")
    public RabbitTemplate rabbitTemplateCluster() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getClusterQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="EngineClear_RabbitTemplate")
    public RabbitTemplate rabbitTemplateClearGwToEngine() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getEngineClearQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="NiaIpAlarm_ListenerContainer")
    public SimpleMessageListenerContainer niaIpAlarmLinkageListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(niaIpAlarmQueue());
        listenerContainer.setMessageListener(niaAlarmMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

}
