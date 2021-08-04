package com.nia.engine.service;

import com.nia.engine.vo.*;

import java.util.HashMap;

/**

 * @author
 *
 */
public interface TopologyService {

    /**
     * selectAlarmMstList
     * @param
     * @return  TopologyObject
     */
    TopologyObject selectAlarmMstList(String alarmid) throws Exception;

    TopologyDataVo selectTopology(HashMap<String, String> map) throws Exception;

}
