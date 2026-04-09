package com.nia.ems.linkage.vo.config;

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

    @Value("${spring.rabbitmq.niaAlarmLinkageSendExchange}")
    private String alarmLinkageResultExchange;
    @Value("${spring.rabbitmq.niaAlarmLinkageSendRoutingKey}")
    private String alarmLinkageResultRoutingKey;
    @Value("${spring.rabbitmq.niaAlarmLinkageSendQueue}")
    private String alarmLinkageResultQueue;

    @Value("${spring.rabbitmq.niaPerformanceToAiSendExchange}")
    private String niaPerformanceToAiSendExchange;
    @Value("${spring.rabbitmq.niaPerformanceToAiSendRoutingKey}")
    private String niaPerformanceToAiSendRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceToAiSendQueue}")
    private String niaPerformanceToAiSendQueue;

    @Value("${spring.rabbitmq.niaPerformanceFailExchange}")
    private String niaPerformanceFailExchange;
    @Value("${spring.rabbitmq.niaPerformanceFailRoutingKey}")
    private String niaPerformanceFailRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceFailQueue}")
    private String niaPerformanceFailQueue;
}
