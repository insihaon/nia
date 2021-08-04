package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.common.UtlDateHelper;
import com.nia.ems.linkage.mapper.AlarmMapper;
import com.nia.ems.linkage.mapper.PerformaceMapper;
import com.nia.ems.linkage.service.AlarmService;
import com.nia.ems.linkage.service.PerformanceService;
import com.nia.ems.linkage.service.RoadmEmsMmcService;
import com.nia.ems.linkage.vo.alarm.AlarmVo;
import com.nia.ems.linkage.vo.performance.PerformaceVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;

@Service("PerformanceService")
public class PerformanceServiceImpl implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceServiceImpl.class);

    @Autowired
    @Qualifier("RoadmEmsMmcService")
    private RoadmEmsMmcService roadmEmsMmcService;

    @Autowired
    private PerformaceMapper performaceMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PerformaceVo> performaceVoObjectFactory;

    @Override
    public PerformaceVo pasingPerformanceMsg(StringBuffer sbPerformanceData) {
        LOGGER.info(sbPerformanceData.toString());

        PerformaceVo performaceVo;
        String[] performaceMsgArr = null;
        String[] performaceEquipArr = null;
        String[] performaceMsgInfoArr = null;

        try {
            if(sbPerformanceData != null && sbPerformanceData.length() > 0){
                performaceMsgArr = sbPerformanceData.toString().split("\n");
                performaceEquipArr = performaceMsgArr[0].split("\\s");
                performaceMsgInfoArr = performaceMsgArr[2].split(",");

                performaceVo = performaceVoObjectFactory.getObject();
                performaceVo.setSysname(performaceEquipArr[3]);
                performaceVo.setPort(performaceMsgInfoArr[0].replaceAll("\"","").replaceAll("\\s",""));
                performaceVo.setOcrtime(UtlDateHelper.stringToTimestamp(performaceMsgInfoArr[11] + " " + performaceMsgInfoArr[12]));
                performaceVo.setTmper(performaceMsgInfoArr[2]);
                performaceVo.setRxCur(Double.parseDouble(performaceMsgInfoArr[3]));
                performaceVo.setRxMin(Double.parseDouble(performaceMsgInfoArr[4]));
                performaceVo.setRxAve(Double.parseDouble(performaceMsgInfoArr[5]));
                performaceVo.setTxCur(Double.parseDouble(performaceMsgInfoArr[6]));
                performaceVo.setTxMin(Double.parseDouble(performaceMsgInfoArr[7]));
                performaceVo.setTxMax(Double.parseDouble(performaceMsgInfoArr[8]));
                performaceVo.setRxAve(Double.parseDouble(performaceMsgInfoArr[9]));

                if(performaceMsgInfoArr[1].contains(":")){
                    performaceVo.setUnit(performaceMsgInfoArr[1].split(":")[0]);
                }

               // performaceMapper.insertPerformace(performaceVo);
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerformanceService] pasingPerformanceMsg error : "+ ExceptionUtils.getStackTrace(e)+" <=====");
        }
        return null;
    }

    @Override
    public void performanceDataCheck() {
        LOGGER.info("=====> [PerformanceService] performanceDataCheck <=====");
        String yyyyMMddHH;
        String updateTime;
        int updateCnt = 0;
        Timestamp curTime = null;
        HashMap<String, String> map;

        try {
            Thread.sleep((120*1000));
            updateTime = performaceMapper.selectPerformanceUpdateTime();

            yyyyMMddHH = (UtlDateHelper.getCurrentDateTime()+"").substring(0,14);

            if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                curTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                curTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                curTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()){
                curTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00");
            }

            if(UtlDateHelper.stringToTimestamp(updateTime).getTime() < curTime.getTime()){
                LOGGER.info("=====> [PerformanceService] performanceDataCheck Fail(updateTime : "+updateTime+" / curTime : "+curTime+") <=====");
                roadmEmsMmcService.roadmSipcMMC();
                Thread.sleep(2000);
                roadmEmsMmcService.roadmPmMMC();
            }else{
                map = new HashMap<String, String>();
                map.put("ocrtime", updateTime);
                updateCnt = performaceMapper.selectPerformanceUpdateCnt(map);

                if(updateCnt > 120){
                    LOGGER.info("=====> [PerformanceService] performanceDataCheck Fail(updateTime : "+updateTime+" / curTime : "+curTime+") updateCnt : "+updateCnt+"<=====");
                    roadmEmsMmcService.roadmSipcMMC();
                    Thread.sleep(2000);
                    roadmEmsMmcService.roadmPmMMC();
                }else{
                    LOGGER.info("=====> [PerformanceService] performanceDataCheck OK(updateTime : "+updateTime+" / curTime : "+curTime+") <=====");
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerformanceService] performanceDataCheck error : "+ ExceptionUtils.getStackTrace(e)+" <=====");
        }
    }
}
