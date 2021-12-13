package com.nia.engine.service.impl;

import com.nia.engine.mapper.TopologyMapper;
import com.nia.engine.service.TopologyService;
import com.nia.engine.vo.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**

 * @author
 *
 */
@Service("TopologyService")
@Scope(value = "prototype")
public class TopologyServiceImpl implements TopologyService {
	private final Logger LOGGER = Logger.getLogger(TopologyServiceImpl.class);

	@Autowired
	private TopologyMapper topologyMapper;

    /**
     * selectAlarmMstList
     * @param
     * @return  TopologyObject
     */
    @Override
    public TopologyObject selectAlarmMstList(String alarmid) throws Exception {
        return topologyMapper.selectAlarmMstList(alarmid);
    }

    /**
     * selectTopologyData
     * @param
     * @return  TopologyDataVo
     */
    @Override
    public TopologyDataVo selectTopology(HashMap<String, String> map) throws Exception {
        return topologyMapper.selectTopology(map);
    }

    /**
     * selectE2eTopology
     * @param
     * @return  TopologyObject
     */
    @Override
    public TopologyObject selectE2eTopology(HashMap<String, String> map) throws Exception {
        return topologyMapper.selectE2eTopology(map);
    }
}
