package com.nia.ai.per.ap.service;


import com.nia.ai.per.ap.vo.PerformanceClusterVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgListVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceVo;

import java.util.List;

/**

 * @author
 *
 */
public interface LineMonitoringHdlService {
    void lineMonitoringHdlProcessor(String ocrTime);
    void lineMonitoringSchdulerHdlProcessor();
    void performanceDataCluster(List<RoadmPerformanceOrgVo> pRoadmPerformanaceOrgList);
    void createCluster(RoadmPerformanceOrgVo roadmPerformanceVo);
}
