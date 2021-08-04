package com.nia.rca.cluster.preprocessor.service.cluster;

import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import com.nia.rca.cluster.preprocessor.vo.ClusterObject;
import com.nia.rca.cluster.preprocessor.vo.TmpClusterObject;

/**

 * @author
 *
 */
public interface ClusterService {

	/**
	 * 클러스터 key 생성
	 * @param
	 * @return String
	 */
	String selectTmpClusterKey() throws Exception;
	
	/**
	 * 클러스터 key 생성
	 * @param
	 * @return String
	 */
	String selectClusterKey() throws Exception;

	/**
	 * TMP 클러스터 저장
	 * @param tmpClusterObject
	 * @return
	 */
	void insertTmpCluster(TmpClusterObject tmpClusterObject) throws Exception;
	
	/**
	 * 클러스터 저장
	 * @param clusterObject
	 * @return
	 */
	void insertCluster(ClusterObject clusterObject) throws Exception;

    void startThreadPool(BasicAlarmVo basicAlarmVo) throws Exception;

    void createCluster(BasicAlarmVo basicAlarmVo) throws Exception;

    void removeCluster(String tmpClusterNo) throws Exception;
}
