package com.nia.engine.vo;

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

	@Value("${spring.rabbitmq.engineExchange}")
	private String engineExchange;
	@Value("${spring.rabbitmq.engineRoutingKey}")
	private String engineRoutingKey;
	@Value("${spring.rabbitmq.engineQueue}")
	private String engineQueue;

	@Value("${spring.rabbitmq.engineResultExchange}")
	private String engineResultExchange;
	@Value("${spring.rabbitmq.engineResultQueue}")
	private String engineResultQueue;
	@Value("${spring.rabbitmq.engineResultRoutingKey}")
	private String engineResultRoutingKey;

	@Value("${spring.rabbitmq.engineClearExchange}")
	private String engineClearExchange;
	@Value("${spring.rabbitmq.engineClearQueue}")
	private String engineClearQueue;
	@Value("${spring.rabbitmq.engineClearRoutingKey}")
	private String engineClearRoutingKey;

	@Value("${spring.rabbitmq.engineToUiExchange}")
	private String engineToUiExchange;
	@Value("${spring.rabbitmq.engineToUiQueue}")
	private String engineToUiQueue;
	@Value("${spring.rabbitmq.engineToUiRoutingKey}")
	private String engineToUiRoutingKey;

	@Value("${spring.rabbitmq.niaPerformanceEngineExchange}")
	private String niaPerformanceEngineExchange;
	@Value("${spring.rabbitmq.niaPerformanceEngineRoutingKey}")
	private String niaPerformanceEngineRoutingKey;
	@Value("${spring.rabbitmq.niaPerformanceEngineQueue}")
	private String niaPerformanceEngineQueue;

	@Value("${spring.rabbitmq.uiToEngineExchange}")
	private String UIToEngineExchange;
	@Value("${spring.rabbitmq.uiToEngineRoutingKey}")
	private String UIToEngineRoutingKey;
	@Value("${spring.rabbitmq.uiToEngineQueue}")
	private String UIToEngineQueue;

	@Value("${spring.rabbitmq.aiIpTrafficResultExchange}")
	private String AiIpTrafficResultExchange;
	@Value("${spring.rabbitmq.aiIpTrafficResultRoutingKey}")
	private String AiIpTrafficResultRoutingKey;
	@Value("${spring.rabbitmq.aiIpTrafficResultQueue}")
	private String AiIpTrafficResultQueue;
}
