package com.nia.ems.linkage.thread.impl;


import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.service.RoadmEmsMmcPasingService;
import com.nia.ems.linkage.thread.NiaEmsLinkageThread;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Queue;


@Service("RoadmEmsMmcResultThread")
public class RoadmEmsMmcResultThreadImpl implements NiaEmsLinkageThread {
	private final static Logger LOGGER = Logger.getLogger(RoadmEmsMmcResultThreadImpl.class);

	@Autowired
    @Qualifier("RoadmEmsMmcPasingService")
    private RoadmEmsMmcPasingService roadmEmsMmcPasingService;

	@Autowired
	private DataShareBean dataShareBean;


	@Override
	public void run() {
		LOGGER.info("=====> [RoadmEmsThread] thread run <=====");
		String mmcMsg;
		try {
			while (true) {
				if(!((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_PASING_QUE)).isEmpty()){
                    mmcMsg = ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_PASING_QUE)).poll();

                    if(mmcMsg.contains("AMPPWR")){
                    	roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-PM");
					}

                    if(mmcMsg.contains("<system>:<control>,<state>")){
                    	roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-SIPC");
					}

					if(mmcMsg.contains("<aid>,<bid>:<almcde>,<asev>,<srveff>,<ocrdat>,<ocrtm>,<name>")){
                    	roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-ALM");
					}

					if(mmcMsg.contains("<aid>:<p-bid>,<p-ports>,<act>,<state>,<r_dir>,<t_dir>,<ad_side>,<c-bid>,<c-ports>,<pair-slot>")){
                    	roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-SLOT");
					}

					if(mmcMsg.contains("<aid>,<bid>:<lambda>,<port>,<add-source>,<drop-address>")){
                    	roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-OPC");
					}

					if(mmcMsg.contains("I:<dir>,<act>,<aid_bau>,<aid_pau>,<state>,<p_sid>,<p_dir>,<p_act>,<p_aid_bau>,<p_aid_pau>")){
                    	roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-NETWORK");
					}

					if(mmcMsg.contains("<tid>,<nename>,<connstat>")){
						roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-NET");
					}

					if(mmcMsg.contains("<sysname>,<model>,<sid>,<sw-ver>,<sw-chksum>,<sw-date>,<sw-time>")
						|| mmcMsg.contains("<sysname>,<psu>,<model>,<sid>,<sw-ver>,<sw-chksum>,<sw-date>,<sw-time>")){
						roadmEmsMmcPasingService.roadmMmcMsgPasing(mmcMsg, "RTRV-SYS");
					}

                }
				try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [RoadmEmsMmcResultThreadImpl] thread run() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
			}
		}catch( Exception e ) {
			LOGGER.error("=====> [RoadmEmsMmcResultThreadImpl] run error : "+ExceptionUtils.getStackTrace(e)+" <=====");
		}
	}
}
