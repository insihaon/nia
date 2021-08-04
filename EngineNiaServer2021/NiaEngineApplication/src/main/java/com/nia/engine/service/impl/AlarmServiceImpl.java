package com.nia.engine.service.impl;

import java.util.HashMap;
import java.util.List;

import com.nia.engine.service.AlarmService;
import com.nia.engine.vo.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nia.engine.mapper.AlarmMapper;

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {
	private final Logger LOGGER = Logger.getLogger(AlarmServiceImpl.class);

	@Autowired
	private AlarmMapper alarmMapper;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<TopologyObject> topologyObjectFactory;

	/**
	 * 클러스터 알람 조회
	 * @param
	 * @return  ArrayList<BasicAlarmVo>
	 */
	@Override
	public List<BasicAlarmVo> selectAlarmMstList(String clusterno) throws Exception {
		return alarmMapper.selectAlarmMstList(clusterno);
	}

	/**
	 * 알람 조회
	 * @param
	 * @return BasicAlarmVo
	 */
	@Override
	public BasicAlarmVo selectAlarmInfo(String alarmno) throws Exception {
		BasicAlarmVo basicAlarmVo = null;
		TopologyObject topologyObject;

		if(alarmno != null){
			basicAlarmVo = alarmMapper.selectAlarmInfo(alarmno);

			topologyObject = topologyObjectFactory.getObject();
			if(topologyObject != null && topologyObject.getAlarmno() != null){
				basicAlarmVo.setTopology(topologyObject);
			}
		}

		return basicAlarmVo;
	}

	/**
	 * 클러스터 Topology 조회
	 *
	 * @param
	 * @return ArrayList<BasicAlarmVo>
	 */
	@Override
	public TopologyObject selectAlTopology(String alarmno) throws Exception {
		return alarmMapper.selectAlTopology(alarmno);
	}
}
