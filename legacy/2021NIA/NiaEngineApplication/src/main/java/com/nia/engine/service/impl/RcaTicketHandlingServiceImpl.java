package com.nia.engine.service.impl;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.*;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("RcaTicketHandlingService")
public class RcaTicketHandlingServiceImpl implements RcaTicketHandlingService {
    private final Logger LOGGER = Logger.getLogger(RcaTicketHandlingServiceImpl.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RcaEngineResult> rcaEngineResultFactory;

    @Autowired
    private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
    private RCATicket rcaTicket;

    @Value("${spring.profiles}")
    private String profiles;

    private Map<String, String> properties;

    @Override
    public synchronized void ticketStatusModify(RCATicketHandlingStatus rcaTicketHandlingStatus){
        RcaEngineResult rcaEngineResult;
        String sopId = "";
        String status = "";
        try{
            LOGGER.info(">>>>>>>>> [RcaTicketHandlingService] ticketStatusModify rcaTicketHandlingStatus : " + rcaTicketHandlingStatus.toString()+" <<<<<<<<<<<<<<<<<");

            if("PF".equals(rcaTicketHandlingStatus.getTicketType())){
                switch (rcaTicketHandlingStatus.getStatus()){
                    case "ACK" :
                        status = rcaTicketHandlingStatus.getStatus();

                        sopId = ticketService.selectSopKey();
                        rcaTicketHandlingStatus.setSopId(sopId);

                        rcaTicketHandlingStatus.setSopId(sopId);
                        ticketService.insertSop(rcaTicketHandlingStatus);
                        ticketService.insertSopPerformance(rcaTicketHandlingStatus);
                        break;
                    case "FIN" :
                        status = rcaTicketHandlingStatus.getStatus();

                        ticketService.updateSop(rcaTicketHandlingStatus);
                        break;
                    case "INIT" :
                        status = rcaTicketHandlingStatus.getStatus();

                        ticketService.updateSop(rcaTicketHandlingStatus);
                        break;
                }

                ticketService.updateRcaTicketCurrentState(rcaTicketHandlingStatus);
//                ticketService.insertRCATicketHandlingStatusHist(rcaTicketHandlingStatus);

                rcaEngineResult = rcaEngineResultFactory.getObject();
                rcaEngineResult.setResult("success");
                rcaEngineResult.setTicketId(rcaTicketHandlingStatus.getTicketId());
                rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);

                properties = new HashMap<String, String>();
                properties.put("status", status);
                properties.put("sop_id", rcaTicketHandlingStatus.getSopId());
                rcaEngineResult.setProperties(properties);

                engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);

                if (((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0) {
                    Iterator<RCATicket> itr = ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();

                    while (itr.hasNext()) {
                        rcaTicket = itr.next();

                        if (rcaTicket.getTicketId().equals(rcaTicketHandlingStatus.getTicketId())) {
                            rcaTicket.setStatus(status);
                        }
                    }
                }
            } else if("TRAFFIC".equals(rcaTicketHandlingStatus.getTicketType())) {
                status = rcaTicketHandlingStatus.getStatus();

                sopId = ticketService.selectSopKey();
                rcaTicketHandlingStatus.setSopId(sopId);

                rcaTicketHandlingStatus.setSopId(sopId);
                ticketService.upsertSop(rcaTicketHandlingStatus);
                if("FIN".equals(rcaTicketHandlingStatus.getStatus())) {
                    ticketService.insertSopMail(rcaTicketHandlingStatus);
                }

                ticketService.updateRcaTicketCurrentState(rcaTicketHandlingStatus);
//                ticketService.insertRCATicketHandlingStatusHist(rcaTicketHandlingStatus);

                rcaEngineResult = rcaEngineResultFactory.getObject();
                rcaEngineResult.setResult("success");
                rcaEngineResult.setTicketId(rcaTicketHandlingStatus.getTicketId());
                rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);

                properties = new HashMap<String, String>();
                properties.put("status", status);
                properties.put("sop_id", rcaTicketHandlingStatus.getSopId());
                rcaEngineResult.setProperties(properties);

                engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);

                if (((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0) {
                    Iterator<RCATicket> itr = ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();

                    while (itr.hasNext()) {
                        rcaTicket = itr.next();

                        if (rcaTicket.getTicketId().equals(rcaTicketHandlingStatus.getTicketId())) {
                            rcaTicket.setStatus(status);
                        }
                    }
                }
            } else if("RT".equals(rcaTicketHandlingStatus.getTicketType())) {
                switch (rcaTicketHandlingStatus.getStatus()) {
                    case "ACK":
                        status = rcaTicketHandlingStatus.getStatus();

                        sopId = ticketService.selectSopKey();
                        rcaTicketHandlingStatus.setSopId(sopId);
                        ticketService.insertSop(rcaTicketHandlingStatus);
                        ticketService.insertSopMail(rcaTicketHandlingStatus);
                        break;
                    case "FIN":
                        status = rcaTicketHandlingStatus.getStatus();

                        ticketService.updateSop(rcaTicketHandlingStatus);
                        break;
                }

                ticketService.updateRcaTicketCurrentState(rcaTicketHandlingStatus);
//                ticketService.insertRCATicketHandlingStatusHist(rcaTicketHandlingStatus);

                rcaEngineResult = rcaEngineResultFactory.getObject();
                rcaEngineResult.setResult("success");
                rcaEngineResult.setTicketId(rcaTicketHandlingStatus.getTicketId());
                rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);

                properties = new HashMap<String, String>();
                properties.put("status", status);
                properties.put("sop_id", rcaTicketHandlingStatus.getSopId());
                rcaEngineResult.setProperties(properties);

                engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);

                if (((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0) {
                    Iterator<RCATicket> itr = ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();

                    while (itr.hasNext()) {
                        rcaTicket = itr.next();

                        if (rcaTicket.getTicketId().equals(rcaTicketHandlingStatus.getTicketId())) {
                            rcaTicket.setStatus(status);
                        }
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTicketManager] ticketStatusModify() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public synchronized void removeRcaTicket(String ticketId){
        Iterator<RCATicket> itr;
        RCATicket rcaTicket;
        if(((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0){
            itr = ((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();
            while( itr.hasNext() ) {
                rcaTicket = itr.next();
                if(ticketId.equals(rcaTicket.getTicketId())){
                    itr.remove();
                }
            }
        }
    }
}
