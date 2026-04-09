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

import java.util.ArrayList;
import java.util.List;

@Service
public class NiaSflowDataMsgListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(NiaSflowDataMsgListener.class);

	@Autowired
	@Qualifier("IpSdnSflowService")
	private IpSdnSflowService ipSdnSflowService;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<SflowDataVo> pingRowDataVoObjectFactory;

	@KafkaListener(topics = "telegraf-sflow", groupId = "sflowData")
	public void onMessage(List<String> messages) {
		List<SflowDataVo> sflowDataVoList = new ArrayList<>();

		try {
			LOGGER.info(">>>>>>>>>>[NiaSflowDataMsgListener] Batch size: " + messages.size() + " <<<<<<<<<<<<<<<<<");

			// 1. 메시지 리스트를 순회하며 개별 객체로 변환하고 리스트에 추가
			for (String message : messages) {
				SflowDataVo sflowDataVo = pingRowDataVoObjectFactory.getObject();
				Object obj = UtlCommon.jsonToObject(sflowDataVo, message);
				sflowDataVo = (SflowDataVo)obj;

				// 객체 리스트에 추가
				sflowDataVoList.add(sflowDataVo);
			}

			ipSdnSflowService.sflowDataHdlProcessor(sflowDataVoList);
		} catch (Exception e) {
			LOGGER.error("=====> [NiaSflowDataMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
