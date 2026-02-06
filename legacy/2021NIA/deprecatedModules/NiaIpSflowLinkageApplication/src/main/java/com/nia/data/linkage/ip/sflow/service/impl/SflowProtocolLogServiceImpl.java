package com.nia.data.linkage.ip.sflow.service.impl;

import com.nia.data.linkage.ip.sflow.amqp.AlarmLinkageResultPrdAmqp;
import com.nia.data.linkage.ip.sflow.mapper.linkage.LinkageSflowMapper;
import com.nia.data.linkage.ip.sflow.mapper.nia.NiaSflowMapper;
import com.nia.data.linkage.ip.sflow.service.SflowLogService;
import com.nia.data.linkage.ip.sflow.service.SflowProtocolLogService;
import com.nia.data.linkage.ip.sflow.vo.sflow.SflowLogVo;
import com.nia.data.linkage.ip.sflow.vo.sflow.SflowProtocolLogVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("SflowProtocolLogService")
public class SflowProtocolLogServiceImpl implements SflowProtocolLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SflowProtocolLogServiceImpl.class);

    @Autowired
    private LinkageSflowMapper linkageAlarmMapper;

    @Autowired
    private NiaSflowMapper niaAlarmMapper;

    @Override
    public void getSflowProtocolLogData() {
        LOGGER.info("==========>[SflowProtocolLogService] getSflowProtocolLogData <==============");

        String interrIdx = null;

        ArrayList<SflowProtocolLogVo> sflowProtocolLogVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            interrIdx = niaAlarmMapper.selectSflowYdKey("ipSflowProtocolLogKey");

            if(StringUtils.isNotEmpty(interrIdx)){
                sflowProtocolLogVoList = linkageAlarmMapper.selectSflowProtocolLogList(Integer.parseInt(interrIdx));

                if(sflowProtocolLogVoList != null && sflowProtocolLogVoList.size() > 0) {
                    LOGGER.info("==========>[SflowProtocolLogService] getSflowProtocolLogData sflowProtocolLogVoList("+sflowProtocolLogVoList.size() +") <==============");

                    objectHashMap = new HashMap<>();
                    objectHashMap.put("sflowProtocolLogVoList", sflowProtocolLogVoList);
                    niaAlarmMapper.insertSflowProtocolLog(objectHashMap);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipSflowProtocolLogKey");
                    strHashMap.put("value", sflowProtocolLogVoList.get(sflowProtocolLogVoList.size()-1).getIntIndex()+"");
                    niaAlarmMapper.updateSflowYdKey(strHashMap);

                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [SflowProtocolLogService] getSflowProtocolLogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

}
