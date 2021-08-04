package com.nia.alarm.simulator.thread.impl;


import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nia.alarm.simulator.amqp.AlarmPrdAmqp;
import com.nia.alarm.simulator.common.UtlDateHelper;
import com.nia.alarm.simulator.singleton.SingletoneAlarmData;
import com.nia.alarm.simulator.thread.AlarmThread;
import com.nia.alarm.simulator.vo.AlarmVo;

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
				if(singletoneAlarmData.alarmQuePeek().getReceivetime().getTime() == UtlDateHelper.getCurrentTime().getTime()){
					sendAlarm(singletoneAlarmData.alarmPoll());
				}else if(singletoneAlarmData.alarmQuePeek().getReceivetime().getTime() < UtlDateHelper.getCurrentTime().getTime()){
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
            strLog.append("alarmNo : " + alarmVo.getAlarmno()+"\n");
            strLog.append("alarmtime : " + alarmVo.getAlarmtime()+"\n");
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


