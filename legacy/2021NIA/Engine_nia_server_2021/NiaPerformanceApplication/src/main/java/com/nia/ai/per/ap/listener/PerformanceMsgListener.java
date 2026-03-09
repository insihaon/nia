package com.nia.ai.per.ap.listener;

import com.nia.ai.per.ap.service.LineMonitoringHdlService;
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
public class PerformanceMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMsgListener.class);

	@Autowired
    @Qualifier("LineMonitoringHdlService")
	private LineMonitoringHdlService lineMonitoringHdlService;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[PerformanceMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			Object obj;
			String msg = new String(message.getBody());

		    lineMonitoringHdlService.lineMonitoringHdlProcessor(msg.replaceAll("\"",""));

		} catch (Exception e) {
			LOGGER.error("=====> [PerformanceMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
