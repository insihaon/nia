package com.nia.alarm.ip.simulator.thread.impl;


import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nia.alarm.ip.simulator.amqp.AlarmPrdAmqp;
import com.nia.alarm.ip.simulator.common.UtlDateHelper;
import com.nia.alarm.ip.simulator.singleton.SingletoneAlarmData;
import com.nia.alarm.ip.simulator.thread.AlarmThread;
import com.nia.alarm.ip.simulator.vo.AlarmVo;

@Service("AlarmThread")
public class AlarmThreadImpl implements AlarmThread{

	private final Logger LOGGER = Logger.getLogger(AlarmThreadImpl.class);
	
	@Autowired
	private AlarmPrdAmqp prdAmqp;

	private SingletoneAlarmData singletoneAlarmData = SingletoneAlarmData.getInstance();
	
	@Override
	public void run() {
		LOGGER.info("=====> [AlarmThreadImpl] thread run <=====");
		try {
			while(true){
				alHdlProcessor();
                Thread.sleep(10);

			}	
		} catch (Exception e) {
			LOGGER.error("=====> [AlarmThreadImpl] run error "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}
	
	public void alHdlProcessor(){
		try{
			if(!singletoneAlarmData.getAlarmQue().isEmpty()){
				if(UtlDateHelper.stringToTimestamp(singletoneAlarmData.alarmQuePeek().getDateRegDate()).getTime() == UtlDateHelper.getCurrentTime().getTime()){
					sendAlarm(singletoneAlarmData.alarmPoll());
				}else if(UtlDateHelper.stringToTimestamp(singletoneAlarmData.alarmQuePeek().getDateRegDate()).getTime() < UtlDateHelper.getCurrentTime().getTime()){
                    sendAlarm(singletoneAlarmData.alarmPoll());
				}
			}
		}catch (Exception e) {
			LOGGER.error("=====> [AlarmThreadImpl] alHdlProcessor error "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}
	
	public void sendAlarm(AlarmVo alarmVo){
		try {
            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [AlarmThreadImpl] sendAlarm <=====\n");
            strLog.append("alarmNo : " + alarmVo.getIntErrIdx()+"\n");
            strLog.append("alarmtime : " + alarmVo.getDateRegDate()+"\n");
            strLog.append("ServerCurrentTime : " + UtlDateHelper.getCurrentTime()+"\n");
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog);

			prdAmqp.sendMessageCmd(alarmVo);
		} catch (Exception e) {
			LOGGER.error("=====> [AlarmThreadImpl] sendAlarm error "+ExceptionUtils.getStackTrace(e)+" <=====");
			prdAmqp.sendMessageCmd(alarmVo);
		}
	}
}


