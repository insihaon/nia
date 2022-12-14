package com.nia.ai.traffic.preprocessor.listener;

import com.nia.ai.traffic.preprocessor.common.UtlCommon;
import com.nia.ai.traffic.preprocessor.service.NiaSdnNodeFactorHdlService;
import com.nia.ai.traffic.preprocessor.vo.sdn.factor.NodeFactorJsonVo;
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
public class AiIpSdnNodeFactorMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiIpSdnNodeFactorMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<NodeFactorJsonVo> nodeFactorJsonVoObjectFactory;

	@Autowired
	@Qualifier("NiaSdnNodeFactorHdlService")
	private NiaSdnNodeFactorHdlService niaSdnNodeFactorHdlService;

	@Autowired
	private NodeFactorJsonVo nodeFactorJsonVo;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			Object obj;
			String msg = new String(message.getBody());

			nodeFactorJsonVo = nodeFactorJsonVoObjectFactory.getObject();

			obj = UtlCommon.jsonToObject(nodeFactorJsonVo, msg);
			nodeFactorJsonVo = (NodeFactorJsonVo) obj;

			LOGGER.info(">>>>>>>>>>[AiNodeFactorMsgListener] onMessage : " + nodeFactorJsonVo.getData().getData().size() + " <<<<<<<<<<<<<<<<<");

			niaSdnNodeFactorHdlService.niaSdnNodeFactorHdlProcessor(nodeFactorJsonVo.getData());

		} catch (Exception e) {
			LOGGER.error("=====> [AiNodeFactorMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
