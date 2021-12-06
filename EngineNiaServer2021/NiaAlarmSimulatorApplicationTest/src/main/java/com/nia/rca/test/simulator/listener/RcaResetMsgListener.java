package com.nia.rca.test.simulator.listener;

import com.nia.rca.test.simulator.amqp.UiToEnginePrdAmqp;
import com.nia.rca.test.simulator.mapper.RcaResetMapper;
import com.nia.rca.test.simulator.service.RcaResetService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RcaResetMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(RcaResetMsgListener.class);

	@Autowired
	@Qualifier("RcaResetService")
	private RcaResetService rcaResetService;

	@Autowired
	private UiToEnginePrdAmqp uiToEnginePrdAmqp;


	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[RcaResetMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			uiToEnginePrdAmqp.sendMessageCmd();

			rcaResetService.rcaTicketReStart();

		} catch (Exception e) {
			LOGGER.error("=====> [PerformanceMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
