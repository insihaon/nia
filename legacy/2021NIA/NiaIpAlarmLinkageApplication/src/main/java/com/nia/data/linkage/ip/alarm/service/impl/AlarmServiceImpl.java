package com.nia.data.linkage.ip.alarm.service.impl;

import com.nia.data.linkage.ip.alarm.amqp.AlarmLinkageResultPrdAmqp;
import com.nia.data.linkage.ip.alarm.common.UtlFileReaderWriter;
import com.nia.data.linkage.ip.alarm.mapper.linkage.LinkageAlarmMapper;
import com.nia.data.linkage.ip.alarm.mapper.nia.NiaAlarmMapper;
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

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmLinkageResultPrdAmqp alarmLinkageResultPrdAmqp;

    @Autowired
    private LinkageAlarmMapper linkageAlarmMapper;

    @Autowired
    private NiaAlarmMapper niaAlarmMapper;

    @Override
    public void getAlarmData() {
        LOGGER.info("==========>[AlarmService] getAlarmData <==============");

        String interrIdx = null;

        ArrayList<AlarmVo> alarmList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            interrIdx = niaAlarmMapper.selectAlarmYdKey("ipAlarmKey");

            if(StringUtils.isNotEmpty(interrIdx)){
                alarmList = linkageAlarmMapper.selectAlarmList(Integer.parseInt(interrIdx));

                if(alarmList != null && alarmList.size() > 0) {
                    LOGGER.info("==========>[AlarmService] getAlarmData size ("+alarmList.size()+") <==============");
                    objectHashMap = new HashMap<>();
                    objectHashMap.put("alarmList", alarmList);
                    niaAlarmMapper.insertIpAlarm(objectHashMap);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipAlarmKey");
                    strHashMap.put("value", alarmList.get(alarmList.size()-1).getIntErrIdx()+"");
                    niaAlarmMapper.updateAlarmYdKey(strHashMap);

                    for (AlarmVo alarmVo : alarmList) {
                        alarmLinkageResultPrdAmqp.sendMessageCmd(alarmVo);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [AlarmService] getAlarmData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

}
