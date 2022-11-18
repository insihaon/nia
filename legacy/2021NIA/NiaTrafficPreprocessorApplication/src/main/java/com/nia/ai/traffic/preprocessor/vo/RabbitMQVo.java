package com.nia.ai.traffic.preprocessor.vo;

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

    @Value("${spring.rabbitmq.engineTrafficExchange}")
    private String niaEngineTrafficExchange;
    @Value("${spring.rabbitmq.engineTrafficRoutingKey}")
    private String niaEngineTrafficRoutingKey;
    @Value("${spring.rabbitmq.engineTrafficQueue}")
    private String niaEngineTrafficQueue;

    @Value("${spring.rabbitmq.aiIpAnomalousTrafficResultExchange}")
    private String niaAiIpAnomalousTrafficResultExchange;
    @Value("${spring.rabbitmq.aiIpAnomalousTrafficResultRoutingKey}")
    private String niaAiIpAnomalousTrafficResultRoutingKey;
    @Value("${spring.rabbitmq.aiIpAnomalousTrafficResultQueue}")
    private String niaAiIpAnomalousTrafficResultQueue;

    @Value("${spring.rabbitmq.aiIpNoxiousTrafficResultExchange}")
    private String niaAiIpNoxiousTrafficResultExchange;
    @Value("${spring.rabbitmq.aiIpNoxiousTrafficResultRoutingKey}")
    private String niaAiIpNoxiousTrafficResultRoutingKey;
    @Value("${spring.rabbitmq.aiIpNoxiousTrafficResultQueue}")
    private String niaAiIpNoxiousTrafficResultQueue;

    @Value("${spring.rabbitmq.aiIpSdnTrafficResultExchange}")
    private String aiIpSdnTrafficResultExchange;
    @Value("${spring.rabbitmq.aiIpSdnTrafficResultRoutingKey}")
    private String aiIpSdnTrafficResultRoutingKey;
    @Value("${spring.rabbitmq.aiIpSdnTrafficResultQueue}")
    private String aiISdnTrafficResultQueue;

    @Value("${spring.rabbitmq.aiIpSdnNodeFactorResultExchange}")
    private String aiIpSdnNodeFactorResultExchange;
    @Value("${spring.rabbitmq.aiIpSdnNodeFactorResultRoutingKey}")
    private String aiIpSdnNodeFactorResultRoutingKey;
    @Value("${spring.rabbitmq.aiIpSdnNodeFactorResultQueue}")
    private String aiIpSdnNodeFactorResultQueue;
}
