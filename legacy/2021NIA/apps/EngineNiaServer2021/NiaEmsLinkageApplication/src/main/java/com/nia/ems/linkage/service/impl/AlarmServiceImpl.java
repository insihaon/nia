package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.common.UtlDateHelper;
import com.nia.ems.linkage.mapper.AlarmMapper;
import com.nia.ems.linkage.service.AlarmService;
import com.nia.ems.linkage.vo.alarm.AlarmVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AlarmVo> alarmVoFactory;

    @Override
    public AlarmVo pasingAlarmMsg(StringBuffer sbAlarmData) {
        LOGGER.info(sbAlarmData.toString());
        AlarmVo alarmVo;
        String[] alarmMsgArr = null;
        String[] alarmEquipArr = null;
        String[] alarmLvlArr = null;
        String[] alarmMsgInfoArr = null;

        try {

            if(sbAlarmData != null && sbAlarmData.length() > 0){
                alarmMsgArr = sbAlarmData.toString().split("\n");
                alarmEquipArr = alarmMsgArr[0].split("\\s");
                alarmMsgInfoArr = alarmMsgArr[2].split(",");

                alarmVo = alarmVoFactory.getObject();
                alarmVo.setSysname(alarmEquipArr[3]);
                alarmVo.setReceivetime(UtlDateHelper.stringToTimestamp(alarmEquipArr[4] + " " + alarmEquipArr[5]));
                alarmVo.setAlarmloc(alarmMsgInfoArr[0].replaceAll("\"","").replaceAll("\\s",""));

                if(alarmMsgInfoArr[1].contains(":")){
                    alarmVo.setUnit(alarmMsgInfoArr[1].split(":")[0]);
                    alarmVo.setAlarmlevel(setAlarmLvl(alarmMsgInfoArr[1].split(":")[1]));
                }

                alarmVo.setAlarmmsg(alarmMsgInfoArr[2]);
                alarmVo.setAlarmtime(UtlDateHelper.stringToTimestamp(alarmMsgInfoArr[4] + " " + alarmMsgInfoArr[5].replaceAll("\"","")));
                alarmMapper.insertAlarm(alarmVo);
                LOGGER.info(alarmVo.toString());
            }
        }catch (Exception e){
            LOGGER.error("=====> [AlarmService] pasingAlarmMsg error : "+ ExceptionUtils.getStackTrace(e)+" <=====");
        }
        return null;
    }

    @Override
    public String setAlarmLvl(String level) {
        String resultLevel = "0";

        switch (level){
            case "CL" :
                resultLevel = "1";
                break;
            case "WN" :
                resultLevel = "3";
                break;
            case "MN" :
                resultLevel = "4";
                break;
            case "MJ" :
                resultLevel = "5";
                break;
            case "CR" :
                resultLevel = "7";
                break;
        }
        return resultLevel;
    }

}
