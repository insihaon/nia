package kr.go.ap.linkage.mba.config;

import kr.go.ap.linkage.mba.property.RabbitMqProperties;
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
        CachingConnectionFactory cf = new CachingConnectionFactory(rabbitMqProperties.getAddress());
        cf.setUsername(rabbitMqProperties.getUserName());
        cf.setPassword(rabbitMqProperties.getPassword());
        cf.setPort(rabbitMqProperties.getPort());
        return cf;
    }

    @Bean(name="MbaAiModel_Queue")
    public Queue mbaAiModelQueue() {
        return new Queue(rabbitMqProperties.getMbaAiModelQueue());
    }

    @Bean(name="JsonMessageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="MbaAiModel_RabbitTemplate")
    public RabbitTemplate rabbitTemplateEngineMbaTicket(
            ConnectionFactory connectionFactory,
            @Qualifier("JsonMessageConverter") MessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(rabbitMqProperties.getMbaAiModelQueue());
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
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
}
