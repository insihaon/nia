package kr.go.ap.prepro.mba.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
public class RabbitMqProperties implements Serializable {

    @Value("${spring.rabbitmq.address}")
    private String address;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;

//    @Value("${spring.rabbitmq.mbaAiModelResultExchange}")
//    private String mbaAiModelResultExchange;
//    @Value("${spring.rabbitmq.mbaAiModelResultRoutingKey}")
//    private String mbaAiModelResultRoutingKey;
//    @Value("${spring.rabbitmq.mbaAiModelResultQueue}")
//    private String mbaAiModelResultQueue;

    @Value("${spring.rabbitmq.engineMbaTicketExchange}")
    private String engineMbaTicketExchange;
    @Value("${spring.rabbitmq.engineMbaTicketRoutingKey}")
    private String engineMbaTicketRoutingKey;
    @Value("${spring.rabbitmq.engineMbaTicketQueue}")
    private String engineMbaTicketQueue;
}
