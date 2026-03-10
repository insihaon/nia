package com.nia.ai.per.ap.service.performance;


import com.nia.ai.per.ap.vo.PerformanceRowSignalDataVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgListVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgVo;

import java.util.List;

/**

 * @author
 *
 */
public interface PerformanceService {
    List<RoadmPerformanceOrgVo> rowSignalSectionAndOrderSearch(List<RoadmPerformanceOrgVo> roadmPerformanaceOrgList);
    PerformanceRowSignalDataVo rowSignalDataSortOut(List<RoadmPerformanceOrgVo> roadmPerformanaceOrgList);
}
