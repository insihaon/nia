package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.common.UtlDateHelper;
import com.nia.ems.linkage.mapper.PerformaceMapper;
import com.nia.ems.linkage.service.RoadmEmsMmcService;
import com.nia.ems.linkage.service.RoadmEmsPmMmcCheckService;
import com.nia.ems.linkage.vo.performance.PerformaceVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service("RoadmEmsPmMmcCheckService")
public class RoadmEmsPmMmcCheckServiceImpl implements RoadmEmsPmMmcCheckService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmEmsPmMmcCheckServiceImpl.class);

    @Autowired
    @Qualifier("RoadmEmsMmcService")
    private RoadmEmsMmcService roadmEmsMmcService;

    @Autowired
    private PerformaceMapper performaceMapper;

    @Override
    public void pmMmcLinkageCheck() {
        LOGGER.info("=====> [RoadmEmsPmMmcCheckService] pmMmcLinkageCheck <=====");
        String yyyyMMddHH;
        Timestamp ocrTime = null;
        List<PerformaceVo> performaceVoList = null;
        HashMap<String, String> parameterMap = null;

        try {
            yyyyMMddHH = (UtlDateHelper.getCurrentDateTime()+"").substring(0,14);

            if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()){
                ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00");
            }

            parameterMap = new HashMap<String, String>();
            parameterMap.put("ocrtime", ocrTime+"");
            performaceVoList = performaceMapper.selectPerformanceDataList(parameterMap);

            if(CollectionUtils.isEmpty(performaceVoList)){
                LOGGER.info("=====> [RoadmEmsPmMmcCheckService] pmMmcLinkageCheck performaceVoList size 0<=====");
                roadmEmsMmcService.roadmPmMMC();
            }else{
                LOGGER.info("=====> [RoadmEmsPmMmcCheckService] pmMmcLinkageCheck performaceVoList size("+performaceVoList.size()+")<=====");
            }
        }catch (Exception e){
            LOGGER.error("=====> [RoadmEmsPmMmcCheckService] pmMmcLinkageCheck error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }

    }
}
