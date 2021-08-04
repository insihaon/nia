package com.nia.rca.cluster.preprocessor.service.impl;

import com.nia.rca.cluster.preprocessor.common.RcaCodeInfo;
import com.nia.rca.cluster.preprocessor.data.DataShareBean;
import com.nia.rca.cluster.preprocessor.service.BootSettingService;
import com.nia.rca.cluster.preprocessor.thread.impl.MessageListenerThreadImpl;
import com.nia.rca.cluster.preprocessor.vo.TmpClusterObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("BootSettingService")
public class BootSettingServiceImpl implements BootSettingService {

    private final static Logger LOGGER = Logger.getLogger(BootSettingServiceImpl.class);

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
    @Qualifier("MessageListenerThread")
    private MessageListenerThreadImpl messageListenerThreadImpl;

    @Override
    public void init() {
        createDataShareBean();
        startThread();
    }

    @Override
    public void createDataShareBean(){
        LOGGER.info("==========>[BootSettingService] createDataShareBean <==============");
       // dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_ALARM_QUE, new ConcurrentLinkedQueue<BasicAlarmVo>());
        dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_STOP_CL_QUE, new ConcurrentLinkedQueue<String>());
        dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_TMP_CL_LIST, new Vector<TmpClusterObject>());
    }

    @Override
    public void startThread(){
        new Thread(messageListenerThreadImpl).start();
    }
}
