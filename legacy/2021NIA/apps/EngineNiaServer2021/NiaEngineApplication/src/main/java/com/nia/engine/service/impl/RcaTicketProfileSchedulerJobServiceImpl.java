package com.nia.engine.service.impl;

import com.nia.engine.service.RcaTicketProfileService;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.RCATicket;
import com.nia.engine.vo.RCATicketAl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RcaTicketProfileSchedulerJobService")
public class RcaTicketProfileSchedulerJobServiceImpl {
    private final Logger LOGGER = Logger.getLogger(RcaTicketProfileSchedulerJobServiceImpl.class);

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Autowired
    @Qualifier("RcaTicketProfileService")
    private RcaTicketProfileService rcaTicketProfileService;

    public void profileCheck() {
        LOGGER.info(">>>>>>>>> profileCheck <<<<<<<<<<<<<<");
        List<RCATicket> rcaTicketList;
        List<RCATicketAl> rcaTicketAlList;

        try {
            rcaTicketList = ticketService.selectRcaTicketProfileCheckList();

            if(rcaTicketList != null && rcaTicketList.size() > 0) {
                for (RCATicket rcaTicket : rcaTicketList) {
                    rcaTicketAlList = ticketService.selectRcaTicketAlList(rcaTicket.getTicketId());

                    if(rcaTicketAlList != null && rcaTicketAlList.size() > 0) {
                        rcaTicket.setTicketAlList(rcaTicketAlList);
                        rcaTicketProfileService.profileCheck(rcaTicket);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[RcaTicketProfileService] profileCheck error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }
}
