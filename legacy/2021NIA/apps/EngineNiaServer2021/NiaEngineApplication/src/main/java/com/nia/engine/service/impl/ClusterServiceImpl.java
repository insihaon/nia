package com.nia.engine.service.impl;

import java.util.List;

import com.nia.engine.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nia.engine.mapper.ClusterMapper;
import com.nia.engine.vo.ClusterObject;

@Service("ClusterService")
public class ClusterServiceImpl implements ClusterService {

	@Autowired
	private ClusterMapper clusterMapper;

	/**
	 * 클러스터 조회
	 * @param
	 * @return  ArrayList<ClusterObject>
	 */
	@Override
	public List<ClusterObject> selectClusterData(String tmpClusterSeq) throws Exception {
		return clusterMapper.selectClusterData(tmpClusterSeq);
	}
}
