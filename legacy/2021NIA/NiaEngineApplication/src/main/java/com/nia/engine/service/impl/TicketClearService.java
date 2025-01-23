package com.nia.engine.service.impl;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = "prototype")
public class TicketClearService {

    private final Logger LOGGER = Logger.getLogger(TicketClearService.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicket> rcaTicketFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RcaEngineResult> rcaEngineResultFactory;

    @Autowired
    private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    private Map<String, String> properties;

    public void clearTicketCheck(RCATicket rcaTicket){
        try {
            LOGGER.info(">>>>>>>>> clearTicketCheck rcaTicket : " + rcaTicket.getTicketId()+" <<<<<<<<<<<<<<");
            String ticketId = null;
            RcaEngineResult rcaEngineResult;
            RCATicket tmpRcaTicket;
            ClearTicketResultVo clearTicketResultVo;

            ticketId = ticketService.fcClearTicket(rcaTicket.getTicketId());

            Thread.sleep(100);

            if(ticketId != null){
                clearTicketResultVo = ticketService.fcClearTicketCheck(ticketId);

                if(clearTicketResultVo != null){
                    if(clearTicketResultVo.getisAllClearTicket()){
                        LOGGER.info(">>>>>>>>>clearTicketCheck allClearAlarm : " + rcaTicket.getTicketId() +"<<<<<<<<<<<<<<");

                        properties = new HashMap<String, String>();
                        properties.put("status", RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                        tmpRcaTicket = rcaTicketFactory.getObject();

                        if(rcaTicket.getParentTicketId() != null){
                            tmpRcaTicket.setTicketId(rcaTicket.getParentTicketId());
                        }else {
                            tmpRcaTicket.setTicketId(rcaTicket.getTicketId());
                        }

                        rcaEngineResult = rcaEngineResultFactory.getObject();
                        rcaEngineResult.setTicketId(tmpRcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);
                        rcaEngineResult.setProperties(properties);
                        engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);
                    }

                    if(clearTicketResultVo.getClearAlarmTicket() != null){
                        LOGGER.info(">>>>>>>>>clearTicketCheck ClearTicket : " + clearTicketResultVo.getClearAlarmTicket() +"<<<<<<<<<<<<<<");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[TicketClearService] clearTicketCheck() error("+rcaTicket.getTicketId()+") : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    public void clearAlarm(String clearAlarmVo){
        try {
            LOGGER.info(">>>>>>>>> clearAlarm BasicAlarmVo : " + clearAlarmVo+" <<<<<<<<<<<<<<");
            RCATicket rcaTicket = null;
            RcaEngineResult rcaEngineResult;
            List<String> ticketIdList;
            RCATicket tmpRcaTicket;
            ClearTicketResultVo clearTicketResultVo;

            ticketIdList = ticketService.fcClearAlarm(clearAlarmVo);

            Thread.sleep(100);

            for(String ticketId : ticketIdList) {
                rcaTicket = ticketService.selectRcaTicket(ticketId);

                if(rcaTicket != null){
                    clearTicketResultVo = ticketService.fcClearTicketCheck(ticketId);

                    if(clearTicketResultVo != null){
                        if(clearTicketResultVo.getisAllClearTicket()){
                            LOGGER.info(">>>>>>>>>clearAlarmCheck allClearAlarm : " + rcaTicket.getTicketId() +"<<<<<<<<<<<<<<");

                            tmpRcaTicket = rcaTicketFactory.getObject();

                            properties = new HashMap<String, String>();
                            properties.put("status", RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                            if(rcaTicket.getParentTicketId() != null){
                                tmpRcaTicket.setTicketId(rcaTicket.getParentTicketId());
                            }else {
                                tmpRcaTicket.setTicketId(rcaTicket.getTicketId());
                            }

                            rcaEngineResult = rcaEngineResultFactory.getObject();
                            rcaEngineResult.setTicketId(tmpRcaTicket.getTicketId());
                            rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);
                            rcaEngineResult.setProperties(properties);
                            engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);
                        }
                        if(clearTicketResultVo.getClearAlarmTicket() != null){
                            LOGGER.info(">>>>>>>>>clearAlarmCheck ClearAlarmTicket : " + clearTicketResultVo.getClearAlarmTicket() +"<<<<<<<<<<<<<<");
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[TicketClearService] clearAlarm() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

}





