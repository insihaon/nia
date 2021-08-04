package com.nia.tsdn.service.result.vo;

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
}
