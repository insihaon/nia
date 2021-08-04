package com.nia.ai.performance.send.vo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Getter
public class RabbitMQVo implements Serializable {

    @Value("${spring.rabbitmq.address}")
    private String address;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.niaPerformanceToAiSendExchange}")
    private String niaPerformanceToAiSendExchange;
    @Value("${spring.rabbitmq.niaPerformanceToAiSendRoutingKey}")
    private String niaPerformanceToAiSendRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceToAiSendQueue}")
    private String niaPerformanceToAiSendQueue;
}
