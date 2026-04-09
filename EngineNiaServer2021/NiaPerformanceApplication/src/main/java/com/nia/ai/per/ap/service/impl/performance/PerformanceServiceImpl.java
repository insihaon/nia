package com.nia.ai.per.ap.service.impl.performance;

import com.nia.ai.per.ap.mapper.EquipmentMapper;
import com.nia.ai.per.ap.service.performance.PerformanceService;
import com.nia.ai.per.ap.vo.PerformanceRowSignalDataVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgVo;
import com.nia.ai.per.ap.vo.RoadmRepeaterRouteVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service("PerformanceService")
public class PerformanceServiceImpl implements PerformanceService {
    private final static Logger LOGGER = Logger.getLogger(PerformanceServiceImpl.class);

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<PerformanceRowSignalDataVo> performanceRowSignalDataVoObjectFactory;

    @Override
    public List<RoadmPerformanceOrgVo> rowSignalSectionAndOrderSearch(List<RoadmPerformanceOrgVo> pRoadmPerformanaceOrgList) {
        RoadmRepeaterRouteVo roadmRepeaterRouteVo = null;
        HashMap<String, String> parameterMap = null;
        List<RoadmPerformanceOrgVo> roadmPerformanaceOrgList = pRoadmPerformanaceOrgList;

        try {
            if(roadmPerformanaceOrgList != null && roadmPerformanaceOrgList.size() > 0){
                parameterMap = new HashMap<String, String>();

                for(RoadmPerformanceOrgVo roadmPerformanceOrgVo : roadmPerformanaceOrgList){
                    try {
                        parameterMap.clear();
                        parameterMap.put("sysname", roadmPerformanceOrgVo.getSysname().split("-")[0]);
                        parameterMap.put("ptpname", roadmPerformanceOrgVo.getSysname().split("-")[1] + "-" + roadmPerformanceOrgVo.getPort());
                        parameterMap.put("roadmCode", roadmPerformanceOrgVo.getRoadmCode());
                        roadmRepeaterRouteVo = equipmentMapper.selectRoadmRepeaterRoutenum(parameterMap);
                    }catch (MyBatisSystemException e){
                        LOGGER.error(">>>>>>>>>>>> [PerformanceService] rowSignalSectionAndOrderSearch("+parameterMap.toString()+") error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
                    }

                    if(roadmRepeaterRouteVo != null){
                        roadmPerformanceOrgVo.setTrunkId(roadmRepeaterRouteVo.getTrunkId());
                        roadmPerformanceOrgVo.setRouteNum(roadmRepeaterRouteVo.getRoutenum());
                        roadmPerformanceOrgVo.setDirection(roadmRepeaterRouteVo.getDirection());
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> [PerformanceService] rowSignalSectionAndOrderSearch() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
        return roadmPerformanaceOrgList;
    }

    @Override
    public PerformanceRowSignalDataVo rowSignalDataSortOut(List<RoadmPerformanceOrgVo> roadmPerformanaceOrgList) {
        List<RoadmPerformanceOrgVo> downPerformanaceList;
        List<RoadmPerformanceOrgVo> upPerformanaceList;
        PerformanceRowSignalDataVo performanceRowSignalDataVo = null;
        RoadmPerformanceOrgVo roadmPerformanceOrgVo;
        Boolean isRepeater = false;
        List<RoadmPerformanceOrgVo> tmpRoadmPerformanaceOrgList;

        try{
            if(roadmPerformanaceOrgList != null && roadmPerformanaceOrgList.size() > 0){
                downPerformanaceList = new ArrayList<RoadmPerformanceOrgVo>();
                upPerformanaceList = new ArrayList<RoadmPerformanceOrgVo>();
                tmpRoadmPerformanaceOrgList = new ArrayList<RoadmPerformanceOrgVo>();
                performanceRowSignalDataVo = performanceRowSignalDataVoObjectFactory.getObject();


//                        roadmPerformanceOrgData.setDirection("UP");
//                        upPerformanaceList.add(roadmPerformanceOrgData);
//
//                        roadmPerformanceOrgVo = roadmPerformanceOrgVoObjectFactory.getObject();
//                        roadmPerformanceOrgVo.setRoadmPerformanceOrgVo(roadmPerformanceOrgData);
//                        roadmPerformanceOrgVo.setDirection("DOWN");
//                        downPerformanaceList.add(roadmPerformanceOrgVo);

                for(RoadmPerformanceOrgVo roadmPerformanceOrgData : roadmPerformanaceOrgList){
                    if("ROADM".equals(roadmPerformanceOrgData.getRoadmCode())){
                        if("UP".equals(roadmPerformanceOrgData.getDirection())){
                            upPerformanaceList.add(roadmPerformanceOrgData);
                        }else if("DOWN".equals(roadmPerformanceOrgData.getDirection())){
                            downPerformanaceList.add(roadmPerformanceOrgData);
                        }
                        tmpRoadmPerformanaceOrgList.add(roadmPerformanceOrgData);
                    }else{
                        if("UP".equals(roadmPerformanceOrgData.getDirection())){
                            upPerformanaceList.add(roadmPerformanceOrgData);
                        }else if("DOWN".equals(roadmPerformanceOrgData.getDirection())){
                            downPerformanaceList.add(roadmPerformanceOrgData);
                        }
                        isRepeater = true;
                    }
                }

                if(isRepeater){
                    if(upPerformanaceList != null && upPerformanaceList.size() > 0){
//                        for(RoadmPerformanceOrgVo roadmPerformanceOrgData : upPerformanaceList){
//                            if("ROADM".equals(roadmPerformanceOrgData.getRoadmCode())){
//                                upPerformanaceList.remove(roadmPerformanceOrgData);
//                            }
//                        }

                        Iterator<RoadmPerformanceOrgVo> itr = upPerformanaceList.iterator();

                        while( itr.hasNext() ){
                            roadmPerformanceOrgVo = itr.next();
                            if("ROADM".equals(roadmPerformanceOrgVo.getRoadmCode())){
                                itr.remove();
              //                  upPerformanaceList.remove(roadmPerformanceOrgVo);
                            }
                        }

//                        for(int i = 0; i <= upPerformanaceList.size(); i++){
//                            if("ROADM".equals(upPerformanaceList.get(i).getRoadmCode())){
//                                upPerformanaceList.remove(upPerformanaceList.get(i));
//                            }
//                        }

                        if(tmpRoadmPerformanaceOrgList != null && tmpRoadmPerformanaceOrgList.size() > 0){
                            if(upPerformanaceList != null && upPerformanaceList.size() > 0){
                                for(RoadmPerformanceOrgVo roadmPerformanceOrgData : tmpRoadmPerformanaceOrgList){
                                    roadmPerformanceOrgData.setDirection("UP");
                                }
                                upPerformanaceList.addAll(tmpRoadmPerformanaceOrgList);
                            }
                        }
                    }

                    if(downPerformanaceList != null && downPerformanaceList.size() > 0){
                        Iterator<RoadmPerformanceOrgVo> itr = downPerformanaceList.iterator();

                        while( itr.hasNext() ){
                            roadmPerformanceOrgVo = itr.next();
                            if("ROADM".equals(roadmPerformanceOrgVo.getRoadmCode())){
                                itr.remove();
                            }
                        }

                        if(tmpRoadmPerformanaceOrgList != null && tmpRoadmPerformanaceOrgList.size() > 0){
                            if(downPerformanaceList != null && downPerformanaceList.size() > 0){
                                for(RoadmPerformanceOrgVo roadmPerformanceOrgData : tmpRoadmPerformanaceOrgList){
                                    roadmPerformanceOrgData.setDirection("DOWN");
                                }
                                downPerformanaceList.addAll(tmpRoadmPerformanaceOrgList);
                            }
                        }
                    }
                }

                if(upPerformanaceList != null && upPerformanaceList.size() > 0){
                    performanceRowSignalDataVo.setUpPerformanaceList(upPerformanaceList);
                }

                if(downPerformanaceList != null && downPerformanaceList.size() > 0){
                    performanceRowSignalDataVo.setDownPerformanaceList(downPerformanaceList);
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> [PerformanceService] rowSignalDataSortOut() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
        return performanceRowSignalDataVo;
    }
}

