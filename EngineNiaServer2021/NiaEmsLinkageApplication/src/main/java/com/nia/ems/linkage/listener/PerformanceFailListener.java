package com.nia.ems.linkage.listener;

import com.nia.ems.linkage.service.RoadmEmsMmcService;
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
public class PerformanceFailListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceFailListener.class);
	
    @Autowired
    @Qualifier("RoadmEmsMmcService")
    private RoadmEmsMmcService RoadmEmsMmcService;
	
	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[PerformanceFailListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			Object obj;
			String msg = new String(message.getBody());

			RoadmEmsMmcService.roadmSipcMMC();
		    Thread.sleep((1000*60));
			RoadmEmsMmcService.roadmPmMMC();

		} catch (Exception e) {
			LOGGER.error("=====> [PerformanceFailListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
