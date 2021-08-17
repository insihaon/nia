package com.nia.ping.alarm.preprocessor.service.impl;

import com.nia.ping.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.ping.alarm.preprocessor.service.NiaAlarmHdlService;
import com.nia.ping.alarm.preprocessor.service.alarm.AlarmService;
import com.nia.ping.alarm.preprocessor.service.pasing.PingDataPasingService;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**

 * @author
 *
 */
@Service("NiaAlarmHdlService")
public class NiaAlarmHdlServiceImpl implements NiaAlarmHdlService {
    private final static Logger LOGGER = Logger.getLogger(NiaAlarmHdlService.class);

    @Autowired
	private AlarmMapper alarmMapper;

    @Autowired
    @Qualifier("PingDataPasingService")
    private PingDataPasingService pingDataPasingService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    private HashMap<String, String> parameterMap;

    @Override
    public void niaAlarmHdlProcessor(String pringData) {
        PingAlarmVo pingDataVo = null;

        try {
            pingDataVo = pingDataPasingService.pingDataPasing(pringData);
            alarmService.pingAlarmCheck(pingDataVo);
            LOGGER.info(pingDataVo.toString());
        } catch (Exception e) {
            LOGGER.error("=====> [NiaAlarmHdlService] niaAlarmHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
