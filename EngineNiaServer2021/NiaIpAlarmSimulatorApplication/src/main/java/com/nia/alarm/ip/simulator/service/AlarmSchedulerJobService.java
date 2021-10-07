package com.nia.alarm.ip.simulator.service;

import java.util.ArrayList;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nia.alarm.ip.simulator.mapper.AlarmMapper;
import com.nia.alarm.ip.simulator.singleton.SingletoneAlarmData;
import com.nia.alarm.ip.simulator.vo.AlarmVo;
import org.springframework.stereotype.Service;


@Service
@Qualifier("AlarmSchedulerJobService")
public class AlarmSchedulerJobService {
	private final Logger LOGGER = Logger.getLogger(AlarmSchedulerJobService.class);

	@Autowired
	private AlarmMapper alarmMapper;

	private ArrayList<AlarmVo> alarmList;

	private SingletoneAlarmData singletoneAlarmData = SingletoneAlarmData.getInstance();

	protected void executeInternal() {
		try {
            alarmList = new ArrayList<AlarmVo>();
			alarmList = alarmMapper.selectAlarmList();
            StringBuffer strLog;
			if(alarmList.size() > 0){
//                monitorMapper.updateMonitoring("yd001");
				for(int i=0; i<alarmList.size(); i++){
                    strLog = new StringBuffer();
                    strLog.append("=====> [AlarmSchedulerJobService] insertAlarm <=====\n");
                    strLog.append("alarmNo : " + alarmList.get(i).getIntErrIdx()+"\n");
                    strLog.append("receivetime : " + alarmList.get(i).getDateRaiseDate()+"\n");
                    strLog.append("---------------------------------------------------------------");
                    LOGGER.info(strLog);
                    singletoneAlarmData.alarmQueOffer(alarmList.get(i));
                //    alarmMapper.deleteAlarm(alarmList.get(i).getIntErrIdx());
                //    singletoneAlarmData.alarmListModify("I",alarmList.get(i),null);
                }
			}
		} catch (Exception e) {
			LOGGER.error("=====> [AlarmSchedulerJobService] error "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}

}
