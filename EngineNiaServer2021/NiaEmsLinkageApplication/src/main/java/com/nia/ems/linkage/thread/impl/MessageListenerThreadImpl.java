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
                }catch (InterruptedException e){
                    LOGGER.error("=====> [MessageListenerThreadImpl] thread run() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
			}
		}catch( Exception e ) {
			LOGGER.error("=====> [MessageListenerThreadImpl] run error : "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}

	public void sendMsg(){
		try {
			if(!((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE)).isEmpty()){
//				LOGGER.info("=====> [MessageListenerThreadImpl] DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE " + (Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE) +" <=====");

				((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_QUE))
						.offer(((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE)).poll());
			}

//			if(!((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_MSG_QUE)).isEmpty()){
//				((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_QUE))
//						.offer(((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_MSG_QUE)).poll());
//			}


			if(!((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE)).isEmpty()){
//				LOGGER.info("=====> [MessageListenerThreadImpl] DATA_SHARE_NAME_EMS_MMC_MSG_QUE " + (Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE) +" <=====");

				((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_QUE))
						.offer(((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE)).poll());
			}
		}catch (Exception e){
			LOGGER.error("=====> [MessageListenerThreadImpl] sendMsg error : "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}
}
