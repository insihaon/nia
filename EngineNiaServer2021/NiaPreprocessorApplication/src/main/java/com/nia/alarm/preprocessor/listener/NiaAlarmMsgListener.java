package com.nia.alarm.preprocessor.listener;

import com.nia.alarm.preprocessor.common.UtlCommon;
import com.nia.alarm.preprocessor.service.NiaAlarmHdlService;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
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
public class NiaAlarmMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaAlarmMsgListener.class);
	
	@Autowired
    @Qualifier("NiaAlarmHdlService")
	private NiaAlarmHdlService niaAlarmHdlService;
	
	@Autowired
	private BasicAlarmVo basicAlarmVo;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<BasicAlarmVo> basicAlarmVoFactory;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[NiaAlarmMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			Object obj;
			String msg = new String(message.getBody());
			basicAlarmVo = basicAlarmVoFactory.getObject();

			msg = msg.replaceAll("'","");

		    obj = UtlCommon.jsonToObject(basicAlarmVo, msg);
		    basicAlarmVo = (BasicAlarmVo)obj;

			niaAlarmHdlService.niaAlarmHdlProcessor(basicAlarmVo);
		} catch (Exception e) {
			LOGGER.error("=====> [NiaAlarmMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
