package com.nia.rca.cluster.preprocessor.config;

import com.nia.rca.cluster.preprocessor.listener.ClusterAlarmMsgListener;
import com.nia.rca.cluster.preprocessor.vo.RabbitMQVo;
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

    @Autowired
    private RabbitMQVo rabbitMQVo;

    @Autowired
    private ClusterAlarmMsgListener clusterAlarmMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="Engine_Queue")
    public Queue engineQueue() {
        return new Queue(rabbitMQVo.getEngineQueue());
    }

    @Bean(name="Cluster_Queue")
    public Queue clusterQueue() {
        return new Queue(rabbitMQVo.getClusterQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="Engine_RabbitTemplate")
    public RabbitTemplate rabbitTemplateGwToEngine() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getEngineQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="Cluster_ListenerContainer")
    public SimpleMessageListenerContainer simListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(clusterQueue());
        listenerContainer.setMessageListener(clusterAlarmMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }
}
