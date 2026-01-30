package com.nia.alarm.ip.preprocessor.service.impl;

import com.nia.alarm.ip.preprocessor.service.NiaAlarmHdlService;
import com.nia.alarm.ip.preprocessor.service.alarm.AlarmService;
import com.nia.alarm.ip.preprocessor.service.pasing.PasingService;
import com.nia.alarm.ip.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.AlarmVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;
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
        AlNormalizerVo alNormalizerVo;

        try {
            basicAlarmVo = alarmService.convertAlarmObj(alarmVo);

            alNormalizerVo = alarmService.selectAlNormalizerInfo(basicAlarmVo);

            basicAlarmVo.setAlarmmsgOriginal(basicAlarmVo.getAlarmmsg());

            if(alNormalizerVo != null){
                basicAlarmVo.setAlarmmsg(alNormalizerVo.getNormalizedAlarm());
                basicAlarmVo.setAlarmType(alNormalizerVo.getAlarmType());
            }

            if(alarmVo.getDateClearDate() != null){
                /*
                   DateClearDate 값이 있으면 자동마감 되고 없으면
                   자동마감 되지 않는 설정이 만약 실제 마감여부를 반영했다면 RT에서만큼은 실제로 자동마감이 되는것인데 이것이 맞는지?

                */
                alarmService.clearAlarmSendMessage(basicAlarmVo);
            }

            pasingService.alarmPasing(basicAlarmVo);
        } catch (Exception e) {
            LOGGER.error("=====> [NiaAlarmHdlService] niaAlarmHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
