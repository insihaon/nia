package kr.go.ap.engine.ticket.mba.config;


import kr.go.ap.engine.ticket.mba.property.RabbitMqProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

	private final RabbitMqProperties rabbitMqProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMqProperties.getAddress());
        connectionFactory.setUsername(rabbitMqProperties.getUserName());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        return connectionFactory;
    }

    @Bean(name="EngineToUi_Queue")
    public Queue getEngineToUiQueue() {
        return new Queue(rabbitMqProperties.getEngineToUiQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="EngineToUi_RabbitTemplate")
    public RabbitTemplate rabbitTemplateTicketEngineToUi(
            ConnectionFactory connectionFactory,
            @Qualifier("JsonMessageConverter") MessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(rabbitMqProperties.getEngineToUiQueue());
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            @Qualifier("JsonMessageConverter") MessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        ((Jackson2JsonMessageConverter)converter).setAlwaysConvertToInferredType(true);
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
}
