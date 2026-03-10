package com.nia.rca.cluster.preprocessor.service.cluster;

import com.nia.rca.cluster.preprocessor.service.impl.cluster.AlarmClusterServiceImpl;
import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import com.nia.rca.cluster.preprocessor.vo.TmpClusterObject;

public interface AlarmClusterService {

    AlarmClusterServiceImpl getInstance(BasicAlarmVo basicAlarmVo, TmpClusterObject tmpClusterObject) throws Exception;

    void clustering(BasicAlarmVo basicAlarmVo) throws Exception;

    void clusterMerge() throws Exception;

    void clusterTimerStart() throws Exception;

    void createCluster(BasicAlarmVo basicAlarmVo) throws Exception;

    void clusterThreadStop() throws Exception;

    void clusterTimerStop() throws Exception;

    void onMessage(BasicAlarmVo basicAlarmVo) throws Exception;

    String getTmpClusterKey() throws Exception;
}
