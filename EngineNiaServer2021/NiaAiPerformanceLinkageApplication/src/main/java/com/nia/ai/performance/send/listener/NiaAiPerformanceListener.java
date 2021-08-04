package com.nia.ai.performance.send.listener;

import com.nia.ai.performance.send.service.PerformanceSendService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NiaAiPerformanceListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaAiPerformanceListener.class);
	
	@Autowired
    @Qualifier("PerformanceSendService")
	private PerformanceSendService performanceSendService;
	
	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[NiaAiPerformanceListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			Object obj;
			String msg = new String(message.getBody());

		    performanceSendService.sendPerformanceData(msg.replaceAll("\"",""));
		} catch (Exception e) {
			LOGGER.error("=====> [NiaAiPerformanceListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
