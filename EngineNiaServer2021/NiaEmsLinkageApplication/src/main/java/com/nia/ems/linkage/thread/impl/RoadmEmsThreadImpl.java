package com.nia.ems.linkage.thread.impl;


import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.service.AlarmService;
import com.nia.ems.linkage.service.PerformanceService;
import com.nia.ems.linkage.thread.NiaEmsLinkageThread;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Queue;


@Service("RoadmEmsThread")
public class RoadmEmsThreadImpl implements NiaEmsLinkageThread {
	private final static Logger LOGGER = Logger.getLogger(RoadmEmsThreadImpl.class);

	@Autowired
	@Qualifier("AlarmService")
	private AlarmService alarmService;

	@Autowired
	@Qualifier("PerformanceService")
	private PerformanceService performanceService;

	@Autowired
	private DataShareBean dataShareBean;


	@Override
	public void run() {
		LOGGER.info("=====> [RoadmEmsThreadImpl] thread run <=====");
		try {
			while (true) {
				if(!((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_QUE)).isEmpty()){
					alarmService.pasingAlarmMsg(((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_QUE)).poll());
				}

				if(!((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_QUE)).isEmpty()){
					performanceService.pasingPerformanceMsg(((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_QUE)).poll());
				}
				try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [RoadmEmsThreadImpl] thread run() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
			}
		}catch( Exception e ) {
			LOGGER.error("=====> [RoadmEmsThreadImpl] run error : "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}
}
