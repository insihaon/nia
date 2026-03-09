package com.nia.alarm.ip.preprocessor.vo.config;

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

    @Value("${spring.rabbitmq.niaIpAlarmExchange}")
    private String niaIpAlarmExchange;
    @Value("${spring.rabbitmq.niaIpAlarmRoutingKey}")
    private String niaIpAlarmRoutingKey;
    @Value("${spring.rabbitmq.niaIpAlarmQueue}")
    private String niaIpAlarmQueue;

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
