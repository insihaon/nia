package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.client.TelnetEvt;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.service.RoadmEmsEventMsgService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("RoadmEmsEventMsgService")
public class RoadmEmsEventMsgServiceImpl implements RoadmEmsEventMsgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmEmsEventMsgService.class);

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<TelnetEvt> telnetObjectFactory;

    @Autowired
    private DataShareBean dataShareBean;

    @Value("${spring.ems.roadm_s_ip}")
    private String roadmSIp;
    @Value("${spring.ems.roadm_d_ip}")
    private String roadmDIp;
    @Value("${spring.ems.port_evt}")
    private int port;

    @Override
    public void eventMsgListener() {
        try{
            TelnetEvt telnetEvt = telnetObjectFactory.getObject();
            telnetEvt.setHostPort(roadmDIp, port);
            telnetEvt.openConnection();
            telnetEvt.main_proc();

            ((HashMap<String,Object>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_TELNET_OBJ_QUE)).put("mmcEvt", telnetEvt);

//            Telnet t2 = telnetObjectFactory.getObject();
//            t2.setHostPort(roadmDIp, port);
//            t2.openConnection();
//            t2.main_proc();
        }catch(Exception e){
            LOGGER.error("=====> [RoadmEmsEventMsgService] eventMsgListener error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
