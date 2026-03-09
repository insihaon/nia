package com.nia.ai.per.ap.vo;

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

    @Value("${spring.rabbitmq.niaPerformanceExchange}")
    private String niaPerformanceExchange;
    @Value("${spring.rabbitmq.niaPerformanceRoutingKey}")
    private String niaPerformanceRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceQueue}")
    private String niaPerformanceQueue;

    @Value("${spring.rabbitmq.niaPerformanceEngineExchange}")
    private String niaPerformanceEngineExchange;
    @Value("${spring.rabbitmq.niaPerformanceEngineRoutingKey}")
    private String niaPerformanceEngineRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceEngineQueue}")
    private String niaPerformanceEngineQueue;
}
