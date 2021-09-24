package com.nia.alarm.ip.preprocessor.service.topology;

import com.nia.alarm.ip.preprocessor.vo.topology.*;

import java.util.HashMap;

/**

 * @author
 *
 */
public interface TopologyService {


    TopologyTmpVo selectTopologyList(HashMap<String, String> map);
}
