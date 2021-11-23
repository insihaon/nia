package com.nia.ai.traffic.preprocessor.config;

import com.nia.ai.traffic.preprocessor.listener.AiAnomalousTrafficeMsgListener;
import com.nia.ai.traffic.preprocessor.listener.AiNoxiousTrafficeMsgListener;
import com.nia.ai.traffic.preprocessor.vo.RabbitMQVo;
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
	private AiAnomalousTrafficeMsgListener aiAnomalousTrafficeMsgListener;

	@Autowired
	private AiNoxiousTrafficeMsgListener aiNoxiousTrafficeMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="NiaAiIpAnomalousTrafficResult_Queue")
    public Queue getNiaAiIpAnomalousTrafficResultQueue() {
        return new Queue(rabbitMQVo.getNiaAiIpAnomalousTrafficResultQueue());
    }

    @Bean(name="NiaAiIpNoxiousTrafficResult_Queue")
    public Queue getNiaAiIpNoxiousTrafficResultQueue() {
        return new Queue(rabbitMQVo.getNiaAiIpNoxiousTrafficResultQueue());
    }

    @Bean(name="NiaEngineTraffic_Queue")
    public Queue getNiaEngineTrafficQueue() {
        return new Queue(rabbitMQVo.getNiaEngineTrafficQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaEngineTraffic_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaEngineTraffic() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaEngineTrafficQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="NiaAiIpAnomalousTrafficResult_ListenerContainer")
    public SimpleMessageListenerContainer anomalousTrafficListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(getNiaAiIpAnomalousTrafficResultQueue());
        listenerContainer.setMessageListener(aiAnomalousTrafficeMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="NiaAiIpNoxiousTrafficResult_ListenerContainer")
    public SimpleMessageListenerContainer noxiousListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(getNiaAiIpNoxiousTrafficResultQueue());
        listenerContainer.setMessageListener(aiNoxiousTrafficeMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }
}
