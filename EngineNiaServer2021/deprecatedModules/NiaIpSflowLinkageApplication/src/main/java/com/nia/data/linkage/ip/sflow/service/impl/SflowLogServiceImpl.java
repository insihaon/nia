package com.nia.data.linkage.ip.sflow.service.impl;

import com.nia.data.linkage.ip.sflow.amqp.AlarmLinkageResultPrdAmqp;
import com.nia.data.linkage.ip.sflow.common.UtlDateHelper;
import com.nia.data.linkage.ip.sflow.mapper.linkage.LinkageSflowMapper;
import com.nia.data.linkage.ip.sflow.mapper.nia.NiaSflowMapper;
import com.nia.data.linkage.ip.sflow.service.SflowLogService;
import com.nia.data.linkage.ip.sflow.vo.sflow.SflowLogVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;

import java.util.ArrayList;
import java.util.HashMap;

@Service("SflowLogService")
public class SflowLogServiceImpl implements SflowLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SflowLogServiceImpl.class);

    @Autowired
    private LinkageSflowMapper linkageSflowMapper;

    @Autowired
    private NiaSflowMapper niaSflowMapper;

    @Override
    public void getSflowLogData() {
        LOGGER.info("==========>[SflowLogService] getSflowLogData <==============");

        String dateRegDate = null;

        ArrayList<SflowLogVo> sflowLogVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        SflowLogVo sflowLogVo;

        try {
            Thread.sleep(120*1000);
            dateRegDate = niaSflowMapper.selectSflowYdKey("ipSflowLogKey");

            LOGGER.info("==========>[SflowLogService] getSflowLogData dateRegDate : "+dateRegDate+" <==============");

            if(StringUtils.isNotEmpty(dateRegDate)){
                sflowLogVo = linkageSflowMapper.selectMaxDateRegDate();

                if(sflowLogVo != null){
                    if(UtlDateHelper.stringToTimestamp(dateRegDate).getTime() < sflowLogVo.getDateRegDate().getTime()){
                        sflowLogVoList = linkageSflowMapper.selectSflowLogList(dateRegDate);

                        if(sflowLogVoList != null && sflowLogVoList.size() > 0) {
                            LOGGER.info("==========>[SflowLogService] getSflowLogData sflowLogVoList("+sflowLogVoList.size() +") <==============");

                            objectHashMap = new HashMap<>();
                            objectHashMap.put("sflowLogVoList", sflowLogVoList);
                            niaSflowMapper.insertSflowLog(objectHashMap);

                            strHashMap = new HashMap<>();
                            strHashMap.put("key", "ipSflowLogKey");
                            strHashMap.put("value", sflowLogVoList.get(sflowLogVoList.size()-1).getDateRegDate()+"");
                            niaSflowMapper.updateSflowYdKey(strHashMap);
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [SflowLogService] getSflowLogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

}
