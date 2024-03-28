package com.codej.nia.mq.handler;

import com.codej.base.utils.CommonUtil;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.codej.nia.mq.properties.NiaRabbitMQProperites;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class NiaUiToEnginePublisher {

	@Autowired
    private NiaRabbitMQProperites rabbitMQProperites;

	@Autowired
	@Qualifier("NiaUiToEngine_RabbitTemplate")
	private RabbitTemplate niaUiToEangineTemplate;

	public void sendMessage(Object data) throws Exception {
		try {
			// log.info("=====> [MQ:sendMessage] : AiOperatorReviewSend data={} <=====", data.toString());
			log.info("[MQ:{}] <<< SendPublisher {} <=====", rabbitMQProperites.getAddress(), rabbitMQProperites.getNiaUiToEngine(),data.toString());
			niaUiToEangineTemplate.convertAndSend(data);
			// Thread.sleep(100);
		} catch (Exception e) {
			log.error("=====> [MQ:sendMessage] : error cause={}, stackTrace={} <=====", ExceptionUtils.getStackTrace(e), e.getCause());
			throw new Exception(CommonUtil.format("전송 실패-{}:{}", this.getClass().getSimpleName(), data.toString()));
		}
	}
}
