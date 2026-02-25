package kr.go.ap.engine.ticket.mba.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
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

    @Value("${spring.rabbitmq.engineMbaTicketExchange}")
    private String engineMbaTicketExchange;
    @Value("${spring.rabbitmq.engineMbaTicketRoutingKey}")
    private String engineMbaTicketRoutingKey;
    @Value("${spring.rabbitmq.engineMbaTicketQueue}")
    private String engineMbaTicketQueue;

    @Value("${spring.rabbitmq.engineToUiExchange}")
    private String engineToUiExchange;
    @Value("${spring.rabbitmq.engineToUiQueue}")
    private String engineToUiQueue;
    @Value("${spring.rabbitmq.engineToUiRoutingKey}")
    private String engineToUiRoutingKey;
}
