package com.nia.rca.cluster.preprocessor.service.impl.alarm;


import com.nia.rca.cluster.preprocessor.mapper.AlarmMapper;
import com.nia.rca.cluster.preprocessor.service.alarm.AlarmService;
import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**

 * @author
 *
 */
@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {
	private final static Logger LOGGER = Logger.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmMapper alarmMapper;

    /**
	 * 알람 저장
	 * @param basicAlarmVo
	 * @return
	 */
	@Override
	public void insertAlarm(BasicAlarmVo basicAlarmVo){
		try{
            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [AlarmService] insertAlarm <=====\n");
            strLog.append("tmpClusterno : " + basicAlarmVo.getTmpClusterno()+"\n");
            strLog.append("clusterno : " + basicAlarmVo.getClusterno()+"\n");
            strLog.append("alarmid : " + basicAlarmVo.getAlarmno()+"\n");
            strLog.append("alarmtime : " + basicAlarmVo.getAlarmtime()+"\n");
            strLog.append("receivetime : " + basicAlarmVo.getReceivetime()+"\n");
            strLog.append("sysname : " + basicAlarmVo.getSysname()+"\n");
            strLog.append("equiptype : " + basicAlarmVo.getEquiptype()+"\n");
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog);

            alarmMapper.insertAlarmMst(basicAlarmVo);

            if(basicAlarmVo.getTopology() != null){
                basicAlarmVo.getTopology().setInsertTime(basicAlarmVo.getReceivetime());
                alarmMapper.insertAlTopology(basicAlarmVo.getTopology());
            }

		}catch (Exception e) {
			LOGGER.error(">>>>>>>>>>[AlarmService] insertAlarm error("+basicAlarmVo.getReceivetime()+") : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
		}
		
	}

    @Override
	public void updateAlarmClusterNo(HashMap<String, String> paramMap) throws Exception {
		alarmMapper.updateAlarmClusterNo(paramMap);
	}

}
