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

    @Autowired
    private NiaEngineTrafficMsgListener niaEngineTrafficMsgListener;

    @Autowired
    private NiaEngineAiNoxTicketInfoListener niaEngineAiNoxTicketInfoListener;

    @Autowired
    private NiaAiAnoToEngineMsgListener niaAiAnoToEngineMsgListener;

    @Autowired
    private NiaAiNoxToEngineMsgListener niaAiNoxToEngineMsgListener;

//    @Autowired
//    private UiSyslogMsgListener uiSyslogMsgListener;


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

    @Bean(name="NiaEngineTraffic_Queue")
    public Queue NiaEngineTrafficQueue() { return new Queue(rabbitMQVo.getNiaEngineTrafficQueue()); }

    @Bean(name="NiaEngineAiNoxTicketInfo_Queue")
    public Queue NiaEngineAiNoxTicketInfoQueue() { return new Queue(rabbitMQVo.getNiaEngineAiNoxTicketInfoQueue()); }

    @Bean(name = "NiaEngineToAiAno_Queue")
    public Queue NiaEngineToAiAnoQueue(){return new Queue(rabbitMQVo.getNiaEngineToAiAnoQueue()); }

    @Bean(name = "NiaEngineToAiNox_Queue")
    public Queue NiaEngineToAiNoxQueue(){return new Queue(rabbitMQVo.getNiaEngineToAiNoxQueue()); }

    @Bean(name = "NiaAiAnoEngine_Queue")
    public Queue NiaAiAnoToEngineQueue(){return new Queue(rabbitMQVo.getNiaAiAnoToEngineQueue()); }

    @Bean(name = "NiaAiNoxToEngine_Queue")
    public Queue NiaAiNoxEngineQueue(){return new Queue(rabbitMQVo.getNiaAiNoxToEngineQueue()); }

//    @Bean(name="UIToEngineSyslog_Queue")
//    public Queue UIToEngineSyslogQueue() { return new Queue(rabbitMQVo.getUIToEngineSyslogQueue()); }


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

    @Bean(name="NiaEngineToAiAno_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaEngineToAiAno() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaEngineToAiAnoQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="NiaEngineToAiNox_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaEngineToAiNox() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaEngineToAiNoxQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }


    @Bean(name="NiaEngineTraffic_RabbitTemplate")
    public RabbitTemplate rabbitTemplateNiaEngineTraffic() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(rabbitMQVo.getNiaEngineTrafficQueue());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    
//     위 : 큐 쏘는 부분  | 아래 : 큐 받는 부분
    
    
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

    @Bean(name="NiaEngineTraffic_ListenerContainer")
    public SimpleMessageListenerContainer NiaEngineTrafficListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(NiaEngineTrafficQueue());
        listenerContainer.setMessageListener(niaEngineTrafficMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="NiaEngineAiNoxTicketInfo_ListenerContainer")
    public SimpleMessageListenerContainer NiaEngineAiNoxTicketInfoListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(NiaEngineAiNoxTicketInfoQueue());
        listenerContainer.setMessageListener(niaEngineAiNoxTicketInfoListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="NiaAiAnoToEngine_ListenerContainer")
    public SimpleMessageListenerContainer NiaEngineToAiAnoListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(NiaAiAnoToEngineQueue());
        listenerContainer.setMessageListener(niaAiAnoToEngineMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean(name="NiaAiNoxToEngine_ListenerContainer")
    public SimpleMessageListenerContainer NiaEngineToAiNoxListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(NiaAiNoxEngineQueue());
        listenerContainer.setMessageListener(niaAiNoxToEngineMsgListener);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

//    @Bean(name="UIToEngineSyslog_ListenerContainer")
//    public SimpleMessageListenerContainer UIToEngineSyslogListenerContainer() {
//        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
//        listenerContainer.setConnectionFactory(connectionFactory());
//        listenerContainer.setQueues(UIToEngineSyslogQueue());
//        listenerContainer.setMessageListener(uiSyslogMsgListener);
//        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        return listenerContainer;
//    }
}
