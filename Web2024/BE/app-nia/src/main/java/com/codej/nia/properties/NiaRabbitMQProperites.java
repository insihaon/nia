package com.codej.nia.properties;

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

    @Value("${spring.rabbitmq.address:incodej-lab.iptime.org}")
    private String address;

    @Value("${spring.rabbitmq.port:6786}")
    private int port;

    @Value("${spring.rabbitmq.username:niaadmin}")
    private String userName;

    @JsonIgnore
    @Value("${spring.rabbitmq.password:niaadmin12#$}")
    private String password;

    @Value("${spring.rabbitmq.niaUiToEngine:nia.UiToEngineIndexDirectly}")
    private String niaUiToEngine;

    @Value("${spring.rabbitmq.niaEngineToUi:nia.EngineToUiIndexDirectly}")
    private String niaEngineToUi;

}
