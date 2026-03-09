package com.nia.engine.service;

import com.nia.engine.vo.ClusterObject;

import java.util.List;

public interface ClusterService {
	
	/**
	 * 클러스터 조회
	 * @param
	 * @return  ArrayList<ClusterObject>
	 */
	List<ClusterObject> selectClusterData(String tmpClusterSeq) throws Exception;
}
