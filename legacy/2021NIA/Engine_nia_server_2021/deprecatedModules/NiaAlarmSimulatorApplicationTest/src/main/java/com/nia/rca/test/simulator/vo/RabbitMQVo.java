package com.nia.rca.test.simulator.vo;

import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
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

	@Value("${spring.rabbitmq.niaTicketReStartExchange}")
	private String niaTicketReStartExchange;
	@Value("${spring.rabbitmq.niaTicketReStartRoutingKey}")
	private String niaTicketReStartRoutingKey;
	@Value("${spring.rabbitmq.niaTicketReStartQueue}")
	private String niaTicketReStartQueue;

	@Value("${spring.rabbitmq.niaPerformanceExchange}")
    private String niaPerformanceExchange;
    @Value("${spring.rabbitmq.niaPerformanceRoutingKey}")
    private String niaPerformanceRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceQueue}")
    private String niaPerformanceQueue;

	@Value("${spring.rabbitmq.niaPerformanceToAiSendExchange}")
    private String niaPerformanceToAiSendExchange;
    @Value("${spring.rabbitmq.niaPerformanceToAiSendRoutingKey}")
    private String niaPerformanceToAiSendRoutingKey;
    @Value("${spring.rabbitmq.niaPerformanceToAiSendQueue}")
    private String niaPerformanceToAiSendQueue;

	@Value("${spring.rabbitmq.uiToEngineExchange}")
	private String uiToEngineExchange;
	@Value("${spring.rabbitmq.uiToEngineQueue}")
	private String uiToEngineQueue;
	@Value("${spring.rabbitmq.uiToEngineRoutingKey}")
	private String uiToEngineRoutingKey;
}
