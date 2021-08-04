package com.nia.alarm.preprocessor.service.impl;

import com.nia.alarm.preprocessor.common.NiaCodeInfo;
import com.nia.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.alarm.preprocessor.service.NiaAlarmHdlService;
import com.nia.alarm.preprocessor.service.alarm.AlarmService;
import com.nia.alarm.preprocessor.service.pasing.PasingService;
import com.nia.alarm.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import org.apache.commons.lang3.StringUtils;
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
    @Qualifier("PasingService")
    private PasingService pasingService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    private HashMap<String, String> parameterMap;

    @Override
    public void niaAlarmHdlProcessor(BasicAlarmVo basicAlarmVo) {
        String alarmno = null;
        AlNormalizerVo alNormalizerVo = null;

        try {
//            parameterMap = new HashMap<String, String>();
//        	parameterMap.put("alarmid", basicAlarmVo.getAlarmno());
//        	parameterMap.put("alarmtime", basicAlarmVo.getAlarmtime()+"");
//            alarmno = alarmMapper.selectAlarmNoCheck(parameterMap);

            basicAlarmVo.setAlarmmsgOriginal(basicAlarmVo.getAlarmmsg());

            if(StringUtils.isEmpty(basicAlarmVo.getEquiptype())){
                basicAlarmVo.setEquiptype("ROADM");
            }

            alNormalizerVo = alarmService.selectAlNormalizerInfo(basicAlarmVo);

            if(alNormalizerVo != null){
                basicAlarmVo.setAlarmmsg(alNormalizerVo.getNormalizedAlarm());
            }

            if(NiaCodeInfo.ALARM_LEVEL_CLEAR.equals(basicAlarmVo.getAlarmlevel())){
                alarmService.clearAlarmSendMessage(basicAlarmVo);
            }else{
                if(StringUtils.isEmpty(alarmno)){
                    pasingService.alarmPasing(basicAlarmVo);
                }
            }

            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [NiaAlarmHdlService] niaAlarmHdlProcessor <=====\n");
            strLog.append("alarmId : " + basicAlarmVo.getAlarmno()+"\n");
            strLog.append("alarmtime : " + basicAlarmVo.getAlarmtime()+"\n");
            strLog.append("receivetime : " + basicAlarmVo.getReceivetime()+"\n");
            strLog.append("alarmLvl : " + basicAlarmVo.getAlarmlevel()+"\n");
            strLog.append("alarmloc : " + basicAlarmVo.getAlarmloc()+"\n");
            strLog.append("sysname : " + basicAlarmVo.getSysname()+"\n");
            strLog.append("unit : " + basicAlarmVo.getUnit()+"\n");
            strLog.append("equipType : " + basicAlarmVo.getEquiptype()+"\n");
            strLog.append("alarmMsg : " + basicAlarmVo.getAlarmmsg()+"\n");
            if(basicAlarmVo.getTopologyObject() != null){
                strLog.append("oppSysname : " + basicAlarmVo.getTopologyObject().getOppSysname()+"\n");
                strLog.append("oppPtpName : " + basicAlarmVo.getTopologyObject().getOppPtpName()+"\n");
            }
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog.toString());
        } catch (Exception e) {
            LOGGER.error("=====> [NiaAlarmHdlService] niaAlarmHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
