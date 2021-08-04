package com.nia.rca.cluster.preprocessor.thread.impl;

import com.nia.rca.cluster.preprocessor.common.RcaCodeInfo;
import com.nia.rca.cluster.preprocessor.data.DataShareBean;
import com.nia.rca.cluster.preprocessor.service.cluster.ClusterService;
import com.nia.rca.cluster.preprocessor.thread.PreprocessorThread;
import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("MessageListenerThread")
public class MessageListenerThreadImpl implements PreprocessorThread {
	private final static Logger LOGGER = Logger.getLogger(MessageListenerThreadImpl.class);

	@Autowired
    @Qualifier("ClusterService")
	private ClusterService clusterService;

    @Autowired
	private DataShareBean dataShareBean;

	private Random random = new Random();
	private int tmpA=1;

	@Override
	public void run() {
		LOGGER.info("=====> [MessagelistenerThreadImpl] thread run <=====");

		try {
			while (true) {
//                if(!((Queue<BasicAlarmVo>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_ALARM_QUE)).isEmpty()){
//                    clusterService.startThreadPool(((Queue<BasicAlarmVo>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_ALARM_QUE)).poll());
//                }

                if(!((Queue<BasicAlarmVo>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_STOP_CL_QUE)).isEmpty()){
                    clusterService.removeCluster(((Queue<String>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_STOP_CL_QUE)).poll());
                }

                Thread.sleep(50);
                tempMat();
			}
		}catch( Exception e ) {
			LOGGER.error("=====> [MessagelistenerThreadImpl] run error : "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}

	public void tempMat(){
		int result;
		result = tmpA * random.nextInt(100);
		
	}
}
