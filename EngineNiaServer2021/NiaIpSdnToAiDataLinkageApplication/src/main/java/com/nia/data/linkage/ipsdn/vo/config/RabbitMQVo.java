package com.nia.data.linkage.ipsdn.vo.config;

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
}
