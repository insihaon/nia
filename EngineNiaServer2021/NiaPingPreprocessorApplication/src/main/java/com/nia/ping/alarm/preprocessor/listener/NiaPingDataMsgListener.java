package com.nia.ping.alarm.preprocessor.listener;

import com.nia.ping.alarm.preprocessor.common.UtlCommon;
import com.nia.ping.alarm.preprocessor.service.NiaAlarmHdlService;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.ping.PingRowDataVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NiaPingDataMsgListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaPingDataMsgListener.class);

	@Autowired
	@Qualifier("NiaAlarmHdlService")
	private NiaAlarmHdlService niaAlarmHdlService;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<PingRowDataVo> pingRowDataVoObjectFactory;

	@KafkaListener(topics = "telegraf-ping", groupId = "pringData")
	public void onMessage(String message) {
		PingRowDataVo pingRowDataVo;

		try {
			LOGGER.info(">>>>>>>>>>[NiaPingDataMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");
			Object obj;
			pingRowDataVo = pingRowDataVoObjectFactory.getObject();

			obj = UtlCommon.jsonToObject(pingRowDataVo, message);
			pingRowDataVo = (PingRowDataVo)obj;

			niaAlarmHdlService.niaAlarmHdlProcessor(pingRowDataVo);
		} catch (Exception e) {
			LOGGER.error("=====> [NiaPingDataMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
