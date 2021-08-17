package com.nia.ping.alarm.preprocessor.listener;

import com.nia.ping.alarm.preprocessor.service.NiaAlarmHdlService;
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

	@KafkaListener(topics = "telegraf3", groupId = "pringData")
	public void onMessage(String message) {

		try {
			LOGGER.info(">>>>>>>>>>[NiaPingDataMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");
			niaAlarmHdlService.niaAlarmHdlProcessor(message);
		} catch (Exception e) {
			LOGGER.error("=====> [NiaPingDataMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
