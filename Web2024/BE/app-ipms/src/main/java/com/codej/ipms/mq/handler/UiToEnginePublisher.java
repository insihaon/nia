package com.codej.ipms.mq.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import com.codej.base.utils.CommonUtil;
import com.codej.ipms.mq.properties.IpmsRabbitMQProperites;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class UiToEnginePublisher {

	@Autowired
	private IpmsRabbitMQProperites rabbitMQProperites;

	@Autowired
	@Qualifier("UiToEngine_RabbitTemplate")
	private RabbitTemplate uiToEangineTemplate;

	public void sendMessage(Object data) throws Exception {
		try {

			log.info("[MQ:{}] <<< SendPublisher {} <=====", rabbitMQProperites.getAddress(),
					rabbitMQProperites.getUiToEngine(), data.toString());
					uiToEangineTemplate.convertAndSend(data);
		} catch (Exception e) {
			log.error("=====> [MQ:sendMessage] : error cause={}, stackTrace={} <=====", ExceptionUtils.getStackTrace(e),
					e.getCause());
			throw new Exception(CommonUtil.format("전송 실패-{}:{}", this.getClass().getSimpleName(), data.toString()));
		}
	}
}
