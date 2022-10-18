package com.nia.engine.listener;

import com.nia.engine.common.UtlCommon;
import com.nia.engine.service.RcaTrafficTicketService;
import com.nia.engine.vo.aiTraffic.EngineTrafficeResultVo;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NiaEngineTrafficMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaEngineTrafficMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<EngineTrafficeResultVo> engineTrafficeResultVoObjectFactory;

	@Autowired
	@Qualifier("RcaTrafficTicketService")
	private RcaTrafficTicketService rcaTrafficTicketService;

//	@Autowired
//	private AiTicketAmqp aiTicketAmqp;

	@Override
	public void onMessage(Message message, Channel channel) {
		LOGGER.info("==========>[NiaEngineTrafficMsgListener] onMessage : "+new String(message.getBody())+"<==============");

		try {
			Object obj;
			String msg = new String(message.getBody());

			EngineTrafficeResultVo engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
			obj = UtlCommon.jsonToObject(engineTrafficeResultVo, msg);
			engineTrafficeResultVo = (EngineTrafficeResultVo)obj;

			if("anomalous".equals(engineTrafficeResultVo.getGb())){
				rcaTrafficTicketService.createAnomalousTrafficTicket(engineTrafficeResultVo.getPerfListVo());
			}
			else if("noxious".equals(engineTrafficeResultVo.getGb())){
				rcaTrafficTicketService.createNoxiousTrfficTicket(engineTrafficeResultVo.getNoxiousListVo());
			}

//			aiTicketAmqp.sendMessageCmd(engineTrafficeResultVo);
//
//			Thread.sleep(10 * 1000);
		} catch (Exception e) {
			LOGGER.error("==========>[NiaEngineTrafficMsgListener] onMessage error "+e.getMessage()+" <==============");
		}
	}
}
