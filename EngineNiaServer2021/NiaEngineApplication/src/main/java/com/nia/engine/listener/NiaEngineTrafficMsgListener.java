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

	@Override
	public void onMessage(Message message, Channel channel) {
		LOGGER.info("==========>[NiaEngineTrafficMsgListener] onMessage : "+new String(message.getBody())+"<==============");

		try {
			String msg = new String(message.getBody());
			EngineTrafficeResultVo engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
			Object obj = UtlCommon.jsonToObject(engineTrafficeResultVo, msg);
			engineTrafficeResultVo = (EngineTrafficeResultVo)obj;

			switch(engineTrafficeResultVo.getGb()){
				case "anomalous": // 이상트래픽 과거
					LOGGER.error("과거 이상 트래픽 처리입니다. 확인 필요..");
//					rcaTrafficTicketService.createAnomalousTrafficTicket(engineTrafficeResultVo.getPerfListVo());
					break;
				case "noxious": // 유해 트래픽
					rcaTrafficTicketService.createNoxiousTrfficTicket(engineTrafficeResultVo.getNoxiousListVo());
					break;
				case "nodefactor": // nodefactor
					rcaTrafficTicketService.createNodeFactorTicket(engineTrafficeResultVo.getNodeFactorListVo());
					break;
				case "Traffic_N": case "Traffic_P":
					rcaTrafficTicketService.createSdnTrafficTicket(engineTrafficeResultVo.getTrafficListVo(), engineTrafficeResultVo.getGb());
					break;
				case "Traffic_AI": // 이상트래픽 AIB 는 의도적으로 AIB로 함 AI로 넣으면 티켓 발행되는데 우선 막아놓음
					LOGGER.info("==========>[NiaEngineTrafficMsgListener] insert Traffice_AI : "+ new String(message.getBody())+"<==============");
					rcaTrafficTicketService.createSdnTrafficTicket(engineTrafficeResultVo.getTrafficListVo(), engineTrafficeResultVo.getGb());
					break;
				default:
					LOGGER.error("예상치 못한 Gb ..." + engineTrafficeResultVo.getGb());
					break;
			}
		} catch (Exception e) {
			LOGGER.error("==========>[NiaEngineTrafficMsgListener] onMessage error "+e.getMessage()+" <==============");
		}
	}
}

