package com.nia.engine.service.impl;

import com.nia.engine.service.*;
import com.nia.engine.vo.*;
import com.nia.engine.vo.profile.ProfileVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("RcaTicketProfileService")
public class RcaTicketProfileServiceImpl implements RcaTicketProfileService {
    private final Logger LOGGER = Logger.getLogger(RcaTicketProfileServiceImpl.class);

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Override
    public void profileCheck(RCATicket rcaTicket) {
        ProfileVo profileVo = null;
        String networkType = null;

        HashMap<String, String> parameterMap;

        try {
            if(rcaTicket.getTicketAlList().size() > 0) {
                if("ROADM".equals(rcaTicket.getRootCauseDomain()) || "POTN".equals(rcaTicket.getRootCauseDomain())) {
                    networkType = "전송";
                } else if("SWITCH".equals(rcaTicket.getRootCauseDomain())) {
                    networkType = "IP";
                }

                for(RCATicketAl rcaTicketAl : rcaTicket.getTicketAlList()) {
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("networkType", networkType);
//                    parameterMap.put("processingTemplate", "construction");
                    parameterMap.put("processType", rcaTicket.getTicketRcaResultDtlCode());
                    parameterMap.put("nodeId", rcaTicketAl.getRootCauseSysnameA());
                    parameterMap.put("faultTime", rcaTicket.getFaultTime()+"");
                    profileVo = ticketService.selectProfile(parameterMap);

                    if(profileVo == null && StringUtils.isNotEmpty(rcaTicketAl.getRootCauseSysnameZ())) {
                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("networkType", networkType);
                        parameterMap.put("processingTemplate", "construction");
                        parameterMap.put("processType", rcaTicket.getTicketRcaResultDtlCode());
                        parameterMap.put("nodeId", rcaTicketAl.getRootCauseSysnameZ());
                        parameterMap.put("faultTime", rcaTicket.getFaultTime()+"");
                        profileVo = ticketService.selectProfile(parameterMap);
                    }

                    if(profileVo != null) {
                        LOGGER.info("==========>[RcaTicketProfileService] profileCheck ticketId : " + rcaTicket.getTicketId() + ", status : " + profileVo.getAutoRecovery() +  ", profileTitle : " + profileVo.getProfileTitle() + " <==============");
                        rcaTicket.setStatus(profileVo.getAutoRecovery());
                        rcaTicket.setProfileTitle(profileVo.getProfileTitle());

                        ticketService.fcSetTicketStatusAsPofile(rcaTicket);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[RcaTicketProfileService] profileCheck error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }
}
