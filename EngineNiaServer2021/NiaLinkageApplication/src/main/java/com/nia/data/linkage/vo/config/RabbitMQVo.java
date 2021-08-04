package com.nia.data.linkage.vo.config;

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
}
