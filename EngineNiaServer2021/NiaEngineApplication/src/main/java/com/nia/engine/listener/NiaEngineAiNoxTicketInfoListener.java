package com.nia.engine.listener;

import com.nia.engine.common.UtlCommon;
import com.nia.engine.mapper.CommonDataMapper;
import com.nia.engine.service.RcaTrafficTicketService;
import com.nia.engine.vo.aiTraffic.EngineNttTrafficResultVo;
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
public class NiaEngineAiNoxTicketInfoListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaEngineAiNoxTicketInfoListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<EngineNttTrafficResultVo> enhineNttTrafficResultVoObjectFactory;

	@Autowired
	@Qualifier("RcaTrafficTicketService")
	private RcaTrafficTicketService rcaTrafficTicketService;

	@Autowired
	private CommonDataMapper commonMapper;

	@Override
	public void onMessage(Message message, Channel channel) {
		LOGGER.info("==========>[NiaEngineAiNoxTicketInfoListener] onMessage : " + new String(message.getBody())
				+ "<==============");

		try {
			String msg = new String(message.getBody());
			EngineNttTrafficResultVo engineNttTrafficResultVo = enhineNttTrafficResultVoObjectFactory.getObject();
			Object obj = UtlCommon.jsonToObject(engineNttTrafficResultVo, msg);
			engineNttTrafficResultVo = (EngineNttTrafficResultVo) obj;

			switch (engineNttTrafficResultVo.getGb()) {
				case "noxious": // 유해 트래픽
					rcaTrafficTicketService.createNoxiousTrfficAiTicket(engineNttTrafficResultVo);
					break;
				default:
					LOGGER.error("예상치 못한 Gb ..." + engineNttTrafficResultVo.getGb());
					break;
			}

			HashMap<String, String> strHashMap = new HashMap<>();
			strHashMap.put("key", "aiTrafficNoxKey2");
			commonMapper.updateLinkageYdKey(strHashMap);
		} catch (Exception e) {
			LOGGER.error("==========>[NiaEngineAiNoxTicketInfoListener] onMessage error " + e.getMessage()
					+ " <==============");
		}
	}
}
