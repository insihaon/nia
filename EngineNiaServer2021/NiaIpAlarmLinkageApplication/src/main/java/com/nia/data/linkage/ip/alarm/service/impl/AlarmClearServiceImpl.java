package com.nia.data.linkage.ip.alarm.service.impl;

import com.nia.data.linkage.ip.alarm.amqp.AlarmLinkageResultPrdAmqp;
import com.nia.data.linkage.ip.alarm.mapper.linkage.LinkageAlarmMapper;
import com.nia.data.linkage.ip.alarm.mapper.nia.NiaAlarmMapper;
import com.nia.data.linkage.ip.alarm.service.AlarmClearService;
import com.nia.data.linkage.ip.alarm.service.AlarmService;
import com.nia.data.linkage.ip.alarm.vo.alarm.AlarmVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("AlarmClearService")
public class AlarmClearServiceImpl implements AlarmClearService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmClearServiceImpl.class);

    @Autowired
    private AlarmLinkageResultPrdAmqp alarmLinkageResultPrdAmqp;

    @Autowired
    private LinkageAlarmMapper linkageAlarmMapper;

    @Autowired
    private NiaAlarmMapper niaAlarmMapper;

    @Override
    public void getAlarmClearData() {
        LOGGER.info("==========>[AlarmClearService] getAlarmClearData <==============");

        String interrIdx = null;

        ArrayList<AlarmVo> alarmList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            interrIdx = niaAlarmMapper.selectAlarmYdKey("ipAlarmClearKey");

            if(StringUtils.isNotEmpty(interrIdx)){
                alarmList = linkageAlarmMapper.selectAlarmClearList(interrIdx);

                if(alarmList != null && alarmList.size() > 0) {
                    LOGGER.info("==========>[AlarmService] getAlarmClearData size ("+alarmList.size()+") <==============");

                    for(AlarmVo alarmVo : alarmList){
                        strHashMap = new HashMap<>();
                        strHashMap.put("dateClearDate", alarmVo.getDateClearDate());
                        strHashMap.put("intErrIdx", alarmVo.getIntErrIdx()+"");
                        niaAlarmMapper.updateClearAlarmYdKey(strHashMap);
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipAlarmClearKey");
                    strHashMap.put("value", alarmList.get(alarmList.size()-1).getDateClearDate()+"");
                    niaAlarmMapper.updateAlarmYdKey(strHashMap);

                    for (AlarmVo alarmVo : alarmList) {
                        alarmLinkageResultPrdAmqp.sendMessageCmd(alarmVo);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [AlarmClearService] getAlarmClearData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

}
