package com.nia.ai.per.ap.service.impl;

import com.nia.ai.per.ap.amqp.PerformanceToEnginePrdAmqp;
import com.nia.ai.per.ap.common.UtlDateHelper;
import com.nia.ai.per.ap.mapper.EquipmentMapper;
import com.nia.ai.per.ap.mapper.PerformanceMapper;
import com.nia.ai.per.ap.service.LineMonitoringHdlService;

import com.nia.ai.per.ap.service.performance.PerformanceService;
import com.nia.ai.per.ap.service.ticket.TicketService;
import com.nia.ai.per.ap.vo.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("LineMonitoringHdlService")
public class LineMonitoringHdlServiceImpl implements LineMonitoringHdlService {
    private final static Logger LOGGER = Logger.getLogger(LineMonitoringHdlService.class);

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    @Qualifier("PerformanceService")
    private PerformanceService performanceService;

    @Autowired
    private PerformanceToEnginePrdAmqp performanceToEnginePrdAmqp;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<PerformanceClusterVo> performanceClusterVoObjectFactory;

    private List<PerformanceClusterVo> performanceClusterList;

    @Override
    public void lineMonitoringHdlProcessor(String ocrTime) {
        List<RoadmPerformanceOrgVo> roadmPerformanaceList = null;
        PerformanceRowSignalDataVo performanceRowSignalDataVo = null;
        HashMap<String, String> parameterMap = null;
        HashMap<String, Object> parameterObjMap = null;

        try {
            parameterMap = new HashMap<String, String>();
            parameterMap.put("ocrTime", ocrTime);
            roadmPerformanaceList = performanceMapper.selectPerformanceList(parameterMap);

             if(roadmPerformanaceList != null && roadmPerformanaceList.size() > 0){
                roadmPerformanaceList = performanceService.rowSignalSectionAndOrderSearch(roadmPerformanaceList);
                performanceRowSignalDataVo = performanceService.rowSignalDataSortOut(roadmPerformanaceList);


                if(performanceRowSignalDataVo.getDownPerformanaceList() != null && performanceRowSignalDataVo.getDownPerformanaceList().size() > 0){
                    performanceClusterList = new ArrayList<PerformanceClusterVo>();
                    performanceDataCluster(performanceRowSignalDataVo.getDownPerformanaceList());

                    if(performanceClusterList != null){
                        for(PerformanceClusterVo performanceClusterVo : performanceClusterList){
                            if(performanceClusterVo != null){
                                if((performanceClusterVo.getRoadmPerformanceList() != null && performanceClusterVo.getRoadmPerformanceList().size() > 0)){
                                    parameterObjMap = new HashMap<String, Object>();
                                    parameterObjMap.put("roadmPerformanceList", performanceClusterVo.getRoadmPerformanceList());
          //                          performanceMapper.insertRowSignalData(parameterObjMap);

                                    performanceToEnginePrdAmqp.sendMessageCmd(performanceClusterVo);
                                }
                            }
                        }
                    }
                }

                if(performanceRowSignalDataVo.getUpPerformanaceList() != null && performanceRowSignalDataVo.getUpPerformanaceList().size() > 0){
                    performanceClusterList = new ArrayList<PerformanceClusterVo>();
                    performanceDataCluster(performanceRowSignalDataVo.getUpPerformanaceList());

                    if(performanceClusterList != null){
                        for(PerformanceClusterVo performanceClusterVo : performanceClusterList){
                            if(performanceClusterVo != null){
                                if((performanceClusterVo.getRoadmPerformanceList() != null && performanceClusterVo.getRoadmPerformanceList().size() > 0)){
                                    parameterObjMap = new HashMap<String, Object>();
                                    parameterObjMap.put("roadmPerformanceList", performanceClusterVo.getRoadmPerformanceList());

         //                           performanceMapper.insertRowSignalData(parameterObjMap);

                                    performanceToEnginePrdAmqp.sendMessageCmd(performanceClusterVo);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> [LineMonitoringHdlService] lineMonitoringHdlProcessor() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void lineMonitoringSchdulerHdlProcessor() {
        List<RoadmPerformanceOrgVo> roadmPerformanaceList = null;
        PerformanceRowSignalDataVo performanceRowSignalDataVo = null;
        HashMap<String, String> parameterMap = null;
        HashMap<String, Object> parameterObjMap = null;
        String yyyyMMddHH = null;
        Timestamp updateTime = null;

        try {
            yyyyMMddHH = (UtlDateHelper.getCurrentDateTime()+"").substring(0,14);

            if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00");
            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()){
                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00");
            }

            parameterMap = new HashMap<String, String>();
            parameterMap.put("ocrTime", updateTime+"");
            roadmPerformanaceList = performanceMapper.selectPerformanceList(parameterMap);

             if(roadmPerformanaceList != null && roadmPerformanaceList.size() > 0){
                roadmPerformanaceList = performanceService.rowSignalSectionAndOrderSearch(roadmPerformanaceList);
                performanceRowSignalDataVo = performanceService.rowSignalDataSortOut(roadmPerformanaceList);


                if(performanceRowSignalDataVo.getDownPerformanaceList() != null && performanceRowSignalDataVo.getDownPerformanaceList().size() > 0){
                    performanceClusterList = new ArrayList<PerformanceClusterVo>();
                    performanceDataCluster(performanceRowSignalDataVo.getDownPerformanaceList());

                    if(performanceClusterList != null){
                        for(PerformanceClusterVo performanceClusterVo : performanceClusterList){
                            if(performanceClusterVo != null){
                                if((performanceClusterVo.getRoadmPerformanceList() != null && performanceClusterVo.getRoadmPerformanceList().size() > 0)){
                                    parameterObjMap = new HashMap<String, Object>();
                                    parameterObjMap.put("roadmPerformanceList", performanceClusterVo.getRoadmPerformanceList());
          //                          performanceMapper.insertRowSignalData(parameterObjMap);

                                    performanceToEnginePrdAmqp.sendMessageCmd(performanceClusterVo);
                                }
                            }
                        }
                    }
                }

                if(performanceRowSignalDataVo.getUpPerformanaceList() != null && performanceRowSignalDataVo.getUpPerformanaceList().size() > 0){
                    performanceClusterList = new ArrayList<PerformanceClusterVo>();
                    performanceDataCluster(performanceRowSignalDataVo.getUpPerformanaceList());

                    if(performanceClusterList != null){
                        for(PerformanceClusterVo performanceClusterVo : performanceClusterList){
                            if(performanceClusterVo != null){
                                if((performanceClusterVo.getRoadmPerformanceList() != null && performanceClusterVo.getRoadmPerformanceList().size() > 0)){
                                    parameterObjMap = new HashMap<String, Object>();
                                    parameterObjMap.put("roadmPerformanceList", performanceClusterVo.getRoadmPerformanceList());

         //                           performanceMapper.insertRowSignalData(parameterObjMap);

                                    performanceToEnginePrdAmqp.sendMessageCmd(performanceClusterVo);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> [LineMonitoringHdlService] lineMonitoringSchdulerHdlProcessor() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void performanceDataCluster(List<RoadmPerformanceOrgVo> pRoadmPerformanaceOrgList) {
        Boolean isFind = false;

        try {
            if(pRoadmPerformanaceOrgList != null && pRoadmPerformanaceOrgList.size() > 0){
                for(RoadmPerformanceOrgVo roadmPerformanceData : pRoadmPerformanaceOrgList){
                    if(performanceClusterList.size() > 0){
                        for(PerformanceClusterVo cluster : performanceClusterList){
                            if(cluster.getTrunkId().equals(roadmPerformanceData.getTrunkId())){
                                cluster.getRoadmPerformanceList().add(roadmPerformanceData);
                                isFind = true;
                                break;
                            }
                        }

                        if(!isFind){
                            createCluster(roadmPerformanceData);
                            isFind = false;
                        }
                    }else{
                        createCluster(roadmPerformanceData);
                    }
                    isFind = false;
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> [LineMonitoringHdlService] performanceDataCluster() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createCluster(RoadmPerformanceOrgVo roadmPerformanceVo) {
        PerformanceClusterVo performanceClusterVo;

        try {
            performanceClusterVo = performanceClusterVoObjectFactory.getObject();
            performanceClusterVo.setTrunkId(roadmPerformanceVo.getTrunkId());
            performanceClusterVo.setDirection(roadmPerformanceVo.getDirection());
            performanceClusterVo.setRoadmPerformanceList(new ArrayList<RoadmPerformanceOrgVo>());
            performanceClusterVo.getRoadmPerformanceList().add(roadmPerformanceVo);
            performanceClusterList.add(performanceClusterVo);
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> [LineMonitoringHdlService] createCluster() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }
}
