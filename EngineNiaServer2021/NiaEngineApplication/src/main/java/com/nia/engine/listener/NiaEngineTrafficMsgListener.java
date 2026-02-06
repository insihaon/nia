package com.nia.engine.listener;

import com.nia.engine.common.UtlCommon;
import com.nia.engine.mapper.CommonDataMapper;
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

import java.util.HashMap;

@Service
public class NiaEngineTrafficMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaEngineTrafficMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<EngineTrafficeResultVo> engineTrafficeResultVoObjectFactory;

	@Autowired
	@Qualifier("RcaTrafficTicketService")
	private RcaTrafficTicketService rcaTrafficTicketService;

	@Autowired
	private CommonDataMapper commonMapper;

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
					// 현재 절대 사용안되고 있음. 그래서 신규로 Queue 따로 개발					
					LOGGER.info("==========>[NiaEngineTrafficMsgListener] insert " + engineTrafficeResultVo.getGb() + ": " + new String(message.getBody())+"<==============");
					rcaTrafficTicketService.createNoxiousTrfficTicket(engineTrafficeResultVo.getNoxiousListVo());
					break;
				case "nodefactor": // nodefactor
					rcaTrafficTicketService.createNodeFactorTicket(engineTrafficeResultVo.getNodeFactorListVo());
					break;
				case "Traffic_N": case "Traffic_P":
					rcaTrafficTicketService.createSdnTrafficTicket(engineTrafficeResultVo.getTrafficListVo());
					break;
				case "Traffic_AI": case "Traffic_AI_TCA":
					if(engineTrafficeResultVo.getGb().equals("Traffic_AI")){
						HashMap<String, String> strHashMap = new HashMap<>();
						strHashMap.put("key", "aiTrafficAnoKey2");
						commonMapper.updateLinkageYdKey(strHashMap);
					}

					LOGGER.info("==========>[NiaEngineTrafficMsgListener] insert "+ engineTrafficeResultVo.getGb() + ": " + new String(message.getBody())+"<==============");
					rcaTrafficTicketService.createAttAiTicket(engineTrafficeResultVo.getTrafficListVo(), engineTrafficeResultVo.getGb());
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

