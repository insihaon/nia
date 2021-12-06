package com.nia.alarm.ip.simulator.vo;

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

	@Value("${spring.rabbitmq.niaIpAlarmExchange}")
	private String niaIpAlarmExchange;
	@Value("${spring.rabbitmq.niaIpAlarmRoutingKey}")
	private String niaIpAlarmRoutingKey;
	@Value("${spring.rabbitmq.niaIpAlarmQueue}")
	private String niaIpAlarmQueue;

	@Value("${spring.rabbitmq.niaTicketReStartExchange}")
	private String niaTicketReStartExchange;
	@Value("${spring.rabbitmq.niaTicketReStartRoutingKey}")
	private String niaTicketReStartRoutingKey;
	@Value("${spring.rabbitmq.niaTicketReStartQueue}")
	private String niaTicketReStartQueue;

	@Value("${spring.rabbitmq.uiToEngineExchange}")
	private String uiToEngineExchange;
	@Value("${spring.rabbitmq.uiToEngineQueue}")
	private String uiToEngineQueue;
	@Value("${spring.rabbitmq.uiToEngineRoutingKey}")
	private String uiToEngineRoutingKey;
}
