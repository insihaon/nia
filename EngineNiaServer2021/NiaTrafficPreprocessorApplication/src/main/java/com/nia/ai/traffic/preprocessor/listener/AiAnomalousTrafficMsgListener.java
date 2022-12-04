package com.nia.ai.traffic.preprocessor.listener;

import com.nia.ai.traffic.preprocessor.common.UtlCommon;
import com.nia.ai.traffic.preprocessor.service.NiaAnomalousTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficListVo;
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
public class AiAnomalousTrafficMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiAnomalousTrafficMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<AnomalousTrafficListVo> perfListVoObjectFactory;

	@Autowired
	@Qualifier("NiaAnomalousTrafficHdlService")
	private NiaAnomalousTrafficHdlService niaAnomalousTrafficHdlService;

	@Autowired
	private AnomalousTrafficListVo anomalousTrafficListVo;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			Object obj;
			String msg = new String(message.getBody());

			anomalousTrafficListVo = perfListVoObjectFactory.getObject();

			obj = UtlCommon.jsonToObject(anomalousTrafficListVo, msg);
			anomalousTrafficListVo = (AnomalousTrafficListVo)obj;

			LOGGER.info(">>>>>>>>>>[AiAnomalousTrafficeMsgListener] onMessage : " + anomalousTrafficListVo.getData().size() + " <<<<<<<<<<<<<<<<<");

			niaAnomalousTrafficHdlService.niaAnomalousTrafficeHdlProcessor(anomalousTrafficListVo);
		} catch (Exception e) {
			LOGGER.error("=====> [AiAnomalousTrafficeMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
