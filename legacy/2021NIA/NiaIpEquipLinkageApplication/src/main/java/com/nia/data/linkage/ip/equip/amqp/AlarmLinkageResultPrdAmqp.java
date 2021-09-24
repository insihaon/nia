//package com.nia.data.linkage.ip.equip.amqp;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//
///**
// * RabbitMQ Producer
// * @author
// *
// */
//@Configuration()
//public class AlarmLinkageResultPrdAmqp {
//	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmLinkageResultPrdAmqp.class);
//
//	@Autowired
//	@Qualifier("AlarmIpLinkageResult_RabbitTemplate")
//	private RabbitTemplate rabbitTemplate;
//
//	public void sendMessageCmd(AlarmVo alarmVo) {
//		try {
//            LOGGER.info("==========>[AlarmLinkageResultPrdAmqp] sendMessageCmd url: <==============");
//			rabbitTemplate.convertAndSend(alarmVo);
//		} catch (Exception e) {
//			LOGGER.error("=====> [AlarmLinkageResultPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
//		}
//	}
//}
//
