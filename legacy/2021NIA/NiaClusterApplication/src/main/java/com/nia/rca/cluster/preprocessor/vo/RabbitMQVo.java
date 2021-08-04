package com.nia.rca.cluster.preprocessor.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
public class RabbitMQVo implements Serializable {

    @Value("${spring.rabbitmq.address}")
    private String address;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.clusterExchange}")
    private String clusterExchange;
    @Value("${spring.rabbitmq.clusterRoutingKey}")
    private String clusterRoutingKey;
    @Value("${spring.rabbitmq.clusterQueue}")
    private String clusterQueue;

    @Value("${spring.rabbitmq.engineExchange}")
    private String engineExchange;
    @Value("${spring.rabbitmq.engineRoutingKey}")
    private String engineRoutingKey;
    @Value("${spring.rabbitmq.engineQueue}")
    private String engineQueue;


}
