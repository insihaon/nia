package com.nia.rca.test.simulator.service;

import java.util.ArrayList;

import com.nia.rca.test.simulator.amqp.PerformancePrdAmqp;
import com.nia.rca.test.simulator.amqp.PerformanceToAiPrdAmqp;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nia.rca.test.simulator.amqp.AlarmPrdAmqp;
import com.nia.rca.test.simulator.mapper.AlarmMapper;
import com.nia.rca.test.simulator.thread.impl.AlarmThreadImpl;
import com.nia.rca.test.simulator.vo.AlarmVo;

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
	private AlarmPrdAmqp alarmPrdAmqp;

	@Autowired
	private PerformancePrdAmqp performancePrdAmqp;

	@Autowired
	private PerformanceToAiPrdAmqp performanceToAiPrdAmqp;

	@Autowired
	private AlarmVo alarmVo;
	
	@Autowired
	private AlarmThreadImpl alarmThreadImpl;
	
	private ArrayList<AlarmVo> arrayAlarmVoList;
	private int arrayAlarmVoListSize = 0;
	private int alarmListIndex = 0;
	private boolean isSimulator = false;
	private boolean isSendMsg = false;
	private boolean isFirstSendMsg = true;
	private int alarmListSize;
	
	public void alHdlProcessor() {
		try {
            arrayAlarmVoList = alarmMapper.selectAlarmList();

			LOGGER.info("AlarmSimHdlService size : " + arrayAlarmVoList.size());
			for(AlarmVo alarmVo: arrayAlarmVoList){
				alarmPrdAmqp.sendMessageCmd(alarmVo);
			}

			//performancePrdAmqp.sendMessageCmd("2020-11-26 17:00:00");
//			performanceToAiPrdAmqp.sendMessageCmd("2020-11-26 17:00:00");
		}catch (Exception e) {
			LOGGER.error("=====> [AlarmSimHdlService] alHdlProcessor error :  "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
	
	public void alarmSimulatorThreadStart(){
		Thread t = new Thread(alarmThreadImpl);
		t.start();
	}
}
