package com.nia.rca.test.simulator.service;

import com.nia.rca.test.simulator.amqp.AlarmPrdAmqp;
import com.nia.rca.test.simulator.amqp.PerformancePrdAmqp;
import com.nia.rca.test.simulator.amqp.PerformanceToAiPrdAmqp;
import com.nia.rca.test.simulator.amqp.UiToEnginePrdAmqp;
import com.nia.rca.test.simulator.mapper.AlarmMapper;
import com.nia.rca.test.simulator.mapper.RcaResetMapper;
import com.nia.rca.test.simulator.singleton.SingletoneAlarmData;
import com.nia.rca.test.simulator.vo.AlarmVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**

 * @author
 *
 */
@Service("RcaResetService")
public class RcaResetService {
	private final Logger LOGGER = Logger.getLogger(RcaResetService.class);

    @Autowired
    private RcaResetMapper rcaResetMapper;

    @Autowired
    private PerformancePrdAmqp performancePrdAmqp;

    @Autowired
    private UiToEnginePrdAmqp uiToEnginePrdAmqp;

    @Autowired
	private AlarmPrdAmqp alarmPrdAmqp;

    @Autowired
	private PerformanceToAiPrdAmqp performanceToAiPrdAmqp;

	@Autowired
	private AlarmMapper alarmMapper;

    public void rcaTicketReStart(){
		ArrayList<AlarmVo> alarmList;

    	try {
			rcaResetMapper.deleteTicket();
			rcaResetMapper.deleteTicketAl();
			rcaResetMapper.deleteTicketCable();
			rcaResetMapper.deleteOpticalPerformance();
			rcaResetMapper.deleteAlarmTopology();

			uiToEnginePrdAmqp.sendMessageCmd();

		//	alarmSimHdlService.alarmSimulatorThreadStart();

            alarmList = alarmMapper.selectAlarmList();

			//performancePrdAmqp.sendMessageCmd("2020-12-23 00:00:00");
			performanceToAiPrdAmqp.sendMessageCmd("2020-11-26 17:00:00");

			LOGGER.info("RcaResetService alarmList size : " + alarmList.size());
			for(AlarmVo alarmVo: alarmList){
				alarmPrdAmqp.sendMessageCmd(alarmVo);
			}

		}catch (Exception e){
			LOGGER.error("=====> [RcaResetService] rcaTicketReStart error :  "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
