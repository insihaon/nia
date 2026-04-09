package kr.go.ap.linkage.mba.property;

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

    @Value("${spring.rabbitmq.mbaAiModelExchange}")
    private String mbaAiModelExchange;
    @Value("${spring.rabbitmq.mbaAiModelRoutingKey}")
    private String mbaAiModelRoutingKey;
    @Value("${spring.rabbitmq.mbaAiModelQueue}")
    private String mbaAiModelQueue;
}
