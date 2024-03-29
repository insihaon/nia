package com.codej.nia.mq.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codej.base.dto.AppDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Component
@Data
public class NiaRabbitMQProperites {

    @Autowired
    AppDto appDto;

    @Value("${spring.rabbitmq.embedded-server-start:false}")
    private Boolean embeddedEnabled;

    @Value("${spring.rabbitmq.enabled:true}")
    private Boolean enabled; // false: rabbitmq 를 사용 하지 않는다

    @Value("${spring.rabbitmq.address:10.81.208.123}")
    private String address;

    @Value("${spring.rabbitmq.port:5675}")
    private int port;

    @Value("${spring.rabbitmq.username:rcaadmin}")
    private String userName;

    @JsonIgnore
    @Value("${spring.rabbitmq.password:rcaadmin12#$}")
    private String password;

    
    @Value("${spring.rabbitmq.niaUiToEngine:nia.exchangeUiToEngineDirectly}")
    private String niaUiToEngine;

}
