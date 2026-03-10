package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.service.BootSettingService;
import com.nia.ems.linkage.thread.impl.MessageListenerThreadImpl;
import com.nia.ems.linkage.thread.impl.RoadmEmsMmcResultThreadImpl;
import com.nia.ems.linkage.thread.impl.RoadmEmsThreadImpl;
import com.nia.ems.linkage.thread.impl.ServerAliveCheckThreadImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service("BootSettingService")
public class BootSettingServiceImpl implements BootSettingService {

    private final static Logger LOGGER = Logger.getLogger(BootSettingServiceImpl.class);

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
    @Qualifier("MessageListenerThread")
    private MessageListenerThreadImpl messageListenerThread;

    @Autowired
    @Qualifier("ServerAliveCheckThread")
    private ServerAliveCheckThreadImpl serverAliveCheckThread;

    @Autowired
    @Qualifier("RoadmEmsThread")
    private RoadmEmsThreadImpl roadmEmsThread;

    @Autowired
    @Qualifier("RoadmEmsMmcResultThread")
    private RoadmEmsMmcResultThreadImpl roadmEmsMmcResultThread;

    @Override
    public void init() {
        createDataShareBean();
        startThread();
    }

    @Override
    public void createDataShareBean(){
        LOGGER.info("==========>[BootSettingService] createDataShareBean <==============");
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_MSG_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_POTN_ALARM_MSG_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_POTN_PERFORMANCE_MSG_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE, new ConcurrentLinkedQueue<String>());

        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_POTN_ALARM_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_POTN_PERFORMANCE_QUE, new ConcurrentLinkedQueue<StringBuffer>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_QUE, new ConcurrentLinkedQueue<String>());

        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_EVT_RECONNECTION_QUE, new ConcurrentLinkedQueue<String>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_MMC_RECONNECTION_QUE, new ConcurrentLinkedQueue<String>());

        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_PASING_QUE, new ConcurrentLinkedQueue<String>());
        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_TELNET_OBJ_QUE, new HashMap<String,Object>());

        dataShareBean.putData(LinkageCodeInfo.DATA_SHARE_NAME_TID_LIST, new ArrayList<String>());

    }

    @Override
    public void startThread() {
        new Thread(messageListenerThread).start();
        new Thread(serverAliveCheckThread).start();
        new Thread(roadmEmsThread).start();
        new Thread(roadmEmsMmcResultThread).start();

    }
}
