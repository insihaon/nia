package com.nia.rca.test.simulator.thread.impl;

import java.util.Random;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nia.rca.test.simulator.amqp.AlarmPrdAmqp;
import com.nia.rca.test.simulator.common.UtlDateHelper;
import com.nia.rca.test.simulator.singleton.SingletoneAlarmData;
import com.nia.rca.test.simulator.thread.AlarmThread;
import com.nia.rca.test.simulator.vo.AlarmVo;

@Service("AlarmThread")
public class AlarmThreadImpl implements AlarmThread{

	private final Logger LOGGER = Logger.getLogger(AlarmThreadImpl.class);
	
	@Autowired
	private AlarmPrdAmqp prdAmqp;

	@Autowired
	private AlarmVo alarmVo;
	
	private int alarmListSize = 0;
	
	private int tmpA=1;
	
	private Random random = new Random();
	
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
				if(singletoneAlarmData.alarmQuePeek().getAlarmtime().getTime() == UtlDateHelper.getCurrentTime().getTime()){
					sendAlarm(singletoneAlarmData.alarmPoll());
				}else if(singletoneAlarmData.alarmQuePeek().getAlarmtime().getTime() < UtlDateHelper.getCurrentTime().getTime()){
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
	
	public void tempMat(){
		tmpA = 1 * random.nextInt(100);
		
	}
}


