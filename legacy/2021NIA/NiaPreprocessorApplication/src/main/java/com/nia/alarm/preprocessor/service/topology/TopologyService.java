package com.nia.alarm.preprocessor.service.topology;

import com.nia.alarm.preprocessor.vo.topology.*;

import java.util.HashMap;

/**

 * @author
 *
 */
public interface TopologyService {


    TopologyTmpVo selectTopologyList(HashMap<String, String> map);
    UniTopologyObject selectUniTopologyList(HashMap<String, String> map);
    RoadmRepeaterRouteVo selectRoadmTrunkId(HashMap<String, String> map);
    E2eTopologyVo selectE2eTopologyList(HashMap<String, String> map);
}
