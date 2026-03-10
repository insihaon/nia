package com.nia.alarm.ip.preprocessor.listener;

import com.nia.alarm.ip.preprocessor.common.UtlCommon;
import com.nia.alarm.ip.preprocessor.service.NiaAlarmHdlService;
import com.nia.alarm.ip.preprocessor.service.impl.NiaAlarmHdlServiceImpl;
import com.nia.alarm.ip.preprocessor.vo.alarm.AlarmVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;
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
public class NiaAlarmMsgListener implements ChannelAwareMessageListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaAlarmMsgListener.class);
	
	@Autowired
    @Qualifier("NiaAlarmHdlService")
	private NiaAlarmHdlService niaAlarmHdlService;
	
	@Autowired
	private AlarmVo alarmVo;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<AlarmVo> alarmVoObjectFactory;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LOGGER.info(">>>>>>>>>>[NiaAlarmMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			Object obj;
			String msg = new String(message.getBody());
			alarmVo = alarmVoObjectFactory.getObject();

			msg = msg.replaceAll("'","");

		    obj = UtlCommon.jsonToObject(alarmVo, msg);
			alarmVo = (AlarmVo)obj;

			niaAlarmHdlService.niaAlarmHdlProcessor(alarmVo);
		} catch (Exception e) {
			LOGGER.error("=====> [NiaAlarmMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
