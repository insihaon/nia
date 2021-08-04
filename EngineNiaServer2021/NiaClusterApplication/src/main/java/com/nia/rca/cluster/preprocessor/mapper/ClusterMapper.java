package com.nia.rca.cluster.preprocessor.mapper;

import com.nia.rca.cluster.preprocessor.vo.ClusterObject;
import com.nia.rca.cluster.preprocessor.vo.TmpClusterObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClusterMapper{
	
	/**
	 * 클러스터 key 생성
	 * @param
	 * @return  ArrayList<AlarmVo>
	 */
    String selectTmpClusterKey();
	
	/**
	 * 클러스터 key 생성
	 * @param
	 * @return  ArrayList<AlarmVo>
	 */
    String selectClusterKey();
	
	/**
	 * TMP 클러스터 저장
	 * @param vo
	 * @return
	 */
    void insertTmpCluster(TmpClusterObject vo);
	
	/**
	 * 클러스터 저장
	 * @param vo
	 * @return
	 */
    void insertCluster(ClusterObject vo);
}
