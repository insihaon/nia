package com.nia.ems.linkage.thread.impl;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.thread.NiaEmsLinkageThread;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service("MessageListenerThread")
public class MessageListenerThreadImpl implements NiaEmsLinkageThread {
	private final static Logger LOGGER = Logger.getLogger(MessageListenerThreadImpl.class);

	@Autowired
	private DataShareBean dataShareBean;

	@Override
	public void run() {
		LOGGER.info("=====> [MessageListenerThreadImpl] thread run <=====");

		try {
			while (true) {

				sendMsg();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					LOGGER.error("=====> [MessageListenerThreadImpl] thread run() " + ExceptionUtils.getStackTrace(e)
							+ "<=====");
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"=====> [MessageListenerThreadImpl] run error : " + ExceptionUtils.getStackTrace(e) + " <=====");
		}
	}

	private <T> void transferMessage(Queue<T> src, Queue<T> dest) {
		if (src != null && dest != null) {
			T data = src.poll();
			if (data != null) {
				dest.offer(data);
			}
		}
	}

	public void sendMsg() {
		try {
			// 과거코드
			// if(!((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_MSG_QUE)).isEmpty()){
			// ((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_QUE))
			// .offer(((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_MSG_QUE)).poll());
			// }

			// 1. ROADM ALARM 메시지 처리
			Queue<StringBuffer> alarmMsgQue = (Queue<StringBuffer>) dataShareBean
					.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE);
			Queue<StringBuffer> alarmQue = (Queue<StringBuffer>) dataShareBean
					.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_QUE);

			// 2. MMC 메시지 처리
			Queue<String> mmcMsgQue = (Queue<String>) dataShareBean
					.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE);
			Queue<String> mmcQue = (Queue<String>) dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_QUE);

			// 사용 시
			transferMessage(alarmMsgQue, alarmQue);
			transferMessage(mmcMsgQue, mmcQue);
		} catch (Exception e) {
			LOGGER.error("=====> [MessageListenerThreadImpl] sendMsg error : " + ExceptionUtils.getStackTrace(e)
					+ " <=====");
		}
	}
}
