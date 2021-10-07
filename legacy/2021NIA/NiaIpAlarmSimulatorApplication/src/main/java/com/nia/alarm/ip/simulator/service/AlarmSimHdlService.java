package com.nia.alarm.ip.simulator.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nia.alarm.ip.simulator.amqp.AlarmPrdAmqp;
import com.nia.alarm.ip.simulator.mapper.AlarmMapper;
import com.nia.alarm.ip.simulator.thread.impl.AlarmThreadImpl;
import com.nia.alarm.ip.simulator.vo.AlarmVo;

/**

 * @author
 *
 */
@Service
@Qualifier("AlarmSimHdlService")
public class AlarmSimHdlService {
	private final Logger LOGGER = Logger.getLogger(AlarmSimHdlService.class);

    @Autowired
    private AlarmMapper alarmMapper;

	@Autowired
	private AlarmPrdAmqp prdAmqp;
	

	@Autowired
	private AlarmThreadImpl alarmThreadImpl;
	
	private ArrayList<AlarmVo> arrayAlarmVoList;

	public void alHdlProcessor() {
		try {
            arrayAlarmVoList = alarmMapper.selectAlarmList();

			LOGGER.info("AlarmSimHdlService size : " + arrayAlarmVoList.size());
			for(AlarmVo alarmVo: arrayAlarmVoList){
				prdAmqp.sendMessageCmd(alarmVo);
			}

		}catch (Exception e) {
		}
	}
	
	public void alarmSimulatorThreadStart(){
		Thread t = new Thread(alarmThreadImpl);
		t.start();
	}
}
