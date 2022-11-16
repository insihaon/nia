package com.nia.syslog.preprocessor.config;

import com.nia.syslog.preprocessor.listener.SyslogOrgDataMsgListener;
import com.nia.syslog.preprocessor.vo.config.RabbitMQVo;
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
    private SyslogOrgDataMsgListener syslogOrgDataMsgListener;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQVo.getAddress());
        connectionFactory.setUsername(rabbitMQVo.getUserName());
        connectionFactory.setPassword(rabbitMQVo.getPassword());
        connectionFactory.setPort(rabbitMQVo.getPort());
        return connectionFactory;
    }

    @Bean(name="NiaSyslogAlarm_Queue")
    public Queue niaSyslogAlarmQueue() {
        return new Queue(rabbitMQVo.getNiaSyslogAlarmQueue());
    }


    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="NiaSyslogAlarm_ListenerContainer")
    public SimpleMessageListenerContainer niaAlarmLinkageListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(niaSyslogAlarmQueue());
        listenerContainer.setMessageListener(syslogOrgDataMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

}
