package com.codej.ipms.mq.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codej.base.dto.AppDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Component
@Data
public class IpmsRabbitMQProperites {

    @Autowired
    AppDto appDto;

    @Value("${spring.rabbitmq.embedded-server-start:false}")
    private Boolean embeddedEnabled;

    @Value("${spring.rabbitmq.enabled:true}")
    private Boolean enabled; // false: rabbitmq 를 사용 하지 않는다

    @Value("${spring.rabbitmq.address:incodej-lab.iptime.org}")
    private String address;

    @Value("${spring.rabbitmq.port:6786}")
    private int port;

    @Value("${spring.rabbitmq.username:admin}")
    private String userName;

    @JsonIgnore
    @Value("${spring.rabbitmq.password:admin12#$}")
    private String password;

    @Value("${spring.rabbitmq.UiToEngine:UiToEngineIndexDirectly}")
    private String uiToEngine;

    @Value("${spring.rabbitmq.EngineToUi:EngineToUiIndexDirectly}")
    private String engineToUi;

}
