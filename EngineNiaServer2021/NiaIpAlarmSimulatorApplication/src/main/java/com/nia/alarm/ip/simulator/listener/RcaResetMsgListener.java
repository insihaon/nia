package com.nia.alarm.ip.simulator.listener;

import com.rabbitmq.client.Channel;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;

@Service
public class RcaResetMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(RcaResetMsgListener.class);

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[RcaResetMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");


		} catch (Exception e) {
			LOGGER.error("=====> [PerformanceMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
