package com.nia.alarm.simulator.vo;

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
}
