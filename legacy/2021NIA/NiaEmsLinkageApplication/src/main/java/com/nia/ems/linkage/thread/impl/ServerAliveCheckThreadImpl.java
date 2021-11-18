package com.nia.ems.linkage.thread.impl;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.client.TelnetEvt;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.thread.NiaEmsLinkageThread;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Queue;


@Service("ServerAliveCheckThread")
public class ServerAliveCheckThreadImpl implements NiaEmsLinkageThread {
	private final static Logger LOGGER = Logger.getLogger(ServerAliveCheckThreadImpl.class);

    @Autowired
	private DataShareBean dataShareBean;

    @Value("${spring.ems.port_evt}")
    private int port;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<TelnetEvt> telnetObjectFactory;

	@Override
	public void run() {
		LOGGER.info("=====> [ServerAliveCheckThreadImpl] thread run <=====");

		String host;
		TelnetEvt telnet;
		try {
			while (true) {
				if(!((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_EVT_RECONNECTION_QUE)).isEmpty()){

					host = ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_EVT_RECONNECTION_QUE)).poll();

					LOGGER.info("=====> [ServerAliveCheckThreadImpl] stop Evt service : "+host+" <=====");

					Thread.sleep(3000);

				 	telnet = ((TelnetEvt)((HashMap<String,Object>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_TELNET_OBJ_QUE)).get("mmcEvt"));
				 	telnet.closeConnection();

					telnet = telnetObjectFactory.getObject();
					telnet.setHostPort(host, port);
					telnet.openConnection();
					telnet.main_proc();
					((HashMap<String,Object>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_TELNET_OBJ_QUE)).clear();
					((HashMap<String,Object>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_TELNET_OBJ_QUE)).put("mmcEvt", telnet);

					LOGGER.info("=====> [ServerAliveCheckThreadImpl] run Evt service restart("+host+") <=====");
				}
				try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [ServerAliveCheckThreadImpl] thread run() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
			}
		}catch( Exception e ) {
			LOGGER.error("=====> [ServerAliveCheckThreadImpl] run error : "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}
}
