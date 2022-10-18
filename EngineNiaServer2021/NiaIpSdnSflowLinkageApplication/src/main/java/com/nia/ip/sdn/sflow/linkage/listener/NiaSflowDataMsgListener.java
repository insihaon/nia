package com.nia.ip.sdn.sflow.linkage.listener;

import com.nia.ip.sdn.sflow.linkage.common.UtlCommon;
import com.nia.ip.sdn.sflow.linkage.service.IpSdnSflowService;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowDataVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NiaSflowDataMsgListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaSflowDataMsgListener.class);

	@Autowired
	@Qualifier("IpSdnSflowService")
	private IpSdnSflowService ipSdnSflowService;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<SflowDataVo> pingRowDataVoObjectFactory;

	@KafkaListener(topics = "telegraf-sflow", groupId = "sflowData")
	public void onMessage(String message) {
		SflowDataVo sflowDataVo;

		try {
			LOGGER.info(">>>>>>>>>>[NiaSflowDataMsgListener] onMessage <<<<<<<<<<<<<<<<<");
			Object obj;
			sflowDataVo = pingRowDataVoObjectFactory.getObject();

			obj = UtlCommon.jsonToObject(sflowDataVo, message);
			sflowDataVo = (SflowDataVo)obj;

			ipSdnSflowService.sflowDataHdlProcessor(sflowDataVo);
		} catch (Exception e) {
			LOGGER.error("=====> [NiaSflowDataMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
