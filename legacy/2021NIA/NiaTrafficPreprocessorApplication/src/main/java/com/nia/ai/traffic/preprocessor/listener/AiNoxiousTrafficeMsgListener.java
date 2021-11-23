package com.nia.ai.traffic.preprocessor.listener;

import com.nia.ai.traffic.preprocessor.common.UtlCommon;
import com.nia.ai.traffic.preprocessor.service.NiaNoxiousTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficListVo;
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
public class AiNoxiousTrafficeMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiNoxiousTrafficeMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<NoxiousTrafficListVo> noxiousTrafficListVoObjectFactory;

	@Autowired
	@Qualifier("NiaNoxiousTrafficHdlService")
	private NiaNoxiousTrafficHdlService niaNoxiousTrafficHdlService;

	@Autowired
	private NoxiousTrafficListVo noxiousTrafficListVo;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[AiNoxiousTrafficeMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			Object obj;
			String msg = new String(message.getBody());
			noxiousTrafficListVo = noxiousTrafficListVoObjectFactory.getObject();

			obj = UtlCommon.jsonToObject(noxiousTrafficListVo, msg);
			noxiousTrafficListVo = (NoxiousTrafficListVo)obj;

			niaNoxiousTrafficHdlService.niaNoxiousTrafficeHdlProcessor(noxiousTrafficListVo);

		} catch (Exception e) {
			LOGGER.error("=====> [AiNoxiousTrafficeMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
