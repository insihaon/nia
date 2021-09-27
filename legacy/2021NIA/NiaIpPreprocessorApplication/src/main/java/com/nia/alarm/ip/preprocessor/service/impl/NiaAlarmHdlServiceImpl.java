package com.nia.alarm.ip.preprocessor.service.impl;

import com.nia.alarm.ip.preprocessor.service.NiaAlarmHdlService;
import com.nia.alarm.ip.preprocessor.service.alarm.AlarmService;
import com.nia.alarm.ip.preprocessor.service.pasing.PasingService;
import com.nia.alarm.ip.preprocessor.vo.alarm.AlarmVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**

 * @author
 *
 */
@Service("NiaAlarmHdlService")
public class NiaAlarmHdlServiceImpl implements NiaAlarmHdlService {
    private final static Logger LOGGER = Logger.getLogger(NiaAlarmHdlService.class);

    @Autowired
    @Qualifier("PasingService")
    private PasingService pasingService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Override
    public void niaAlarmHdlProcessor(AlarmVo alarmVo) {
        BasicAlarmVo basicAlarmVo;

        try {
            basicAlarmVo = alarmService.convertAlarmObj(alarmVo);

            if(alarmVo.getDateClearDate() != null){
                alarmService.clearAlarmSendMessage(basicAlarmVo);
            }else{
                pasingService.alarmPasing(basicAlarmVo);
            }
        } catch (Exception e) {
            LOGGER.error("=====> [NiaAlarmHdlService] niaAlarmHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
