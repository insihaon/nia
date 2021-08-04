package com.nia.engine.mapper;

import java.util.List;

import com.nia.engine.vo.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClusterMapper{

	/**
	 * 클러스터 조회
	 * @param
	 * @return  ArrayList<ClusterObject>
	 */
    List<ClusterObject> selectClusterData(String tmpClusterSeq);
}
