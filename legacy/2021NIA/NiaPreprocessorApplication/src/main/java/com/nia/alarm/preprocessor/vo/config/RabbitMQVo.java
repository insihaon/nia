package com.nia.alarm.preprocessor.vo.config;

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

    @Value("${spring.rabbitmq.niaAlarmExchange}")
    private String niaAlarmExchange;
    @Value("${spring.rabbitmq.niaAlarmRoutingKey}")
    private String niaAlarmRoutingKey;
    @Value("${spring.rabbitmq.niaAlarmQueue}")
    private String niaAlarmQueue;

    @Value("${spring.rabbitmq.clusterExchange}")
    private String clusterExchange;
    @Value("${spring.rabbitmq.clusterRoutingKey}")
    private String clusterRoutingKey;
    @Value("${spring.rabbitmq.clusterQueue}")
    private String clusterQueue;

    @Value("${spring.rabbitmq.engineClearExchange}")
    private String engineClearExchange;
    @Value("${spring.rabbitmq.engineClearQueue}")
    private String engineClearQueue;
    @Value("${spring.rabbitmq.engineClearRoutingKey}")
    private String engineClearRoutingKey;
}
