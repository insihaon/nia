package com.nia.engine.service.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.RcaTicketManagerService;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("EngineClearSchedulerJobService")
public class EngineClearSchedulerJobServiceImpl {
	private final Logger LOGGER = Logger.getLogger(EngineClearSchedulerJobServiceImpl.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicketHandlingStatus> rcaTicketHandlingStatusFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicket> rcaTicketFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RcaEngineResult> rcaEngineResultFactory;

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Autowired
    @Qualifier("RcaTicketManagerService")
    private RcaTicketManagerService rcaTicketManager;
    @Autowired
    private DataShareBean dataShareBean;

    private HashMap<String, String> parameterMap;

    private List<BasicAlarmVo> clearAlList = new ArrayList<BasicAlarmVo>();
    private Map<String, String> properties;

    @Value("${spring.profiles}")
    private String profiles;

    public void clearTicketCheck(){
        try {
            LOGGER.info(">>>>>>>>> clearTicketCheck <<<<<<<<<<<<<<");
            RcaEngineResult rcaEngineResult;
            RCATicket tmpRcaTicket;
            RCATicket rcaTicket;
            ClearTicketResultVo clearTicketResultVo;
            List<String> clearTicketCheckList;
            RCATicketHandlingStatus handlingStatus;
            String rootCauseType;
            String dtlCode;
            List<String> topasticketNoList;
            List<Map<String, String>> data = new ArrayList<>();

            RCATicketHandlingStatus rcaTicketHandlingStatus;

            clearTicketCheckList = ticketService.selectClearTicketCheckList();

            if (clearTicketCheckList != null && clearTicketCheckList.size() > 0){
                for (String ticketId : clearTicketCheckList) {
                    rcaTicket = ticketService.selectRcaTicket(ticketId);

                    clearTicketResultVo = ticketService.fcClearTicketCheck(ticketId);

                    if(clearTicketResultVo != null){
                        if (clearTicketResultVo.getisAllClearTicket()) {
                            LOGGER.info(">>>>>>>>>clearTicketCheck allClearAlarm : " + rcaTicket.getTicketId() +"<<<<<<<<<<<<<<");
                            rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
                            rcaTicketHandlingStatus.setTicketId(rcaTicket.getTicketId());

                            rcaTicketHandlingStatus.setStatus(RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                            if(rcaTicket.getParentTicketId() != null){
                                rcaTicketHandlingStatus.setTicketId(rcaTicket.getParentTicketId());
                            }

                            tmpRcaTicket = rcaTicketFactory.getObject();

                            if(rcaTicket.getParentTicketId() != null){
                                tmpRcaTicket.setTicketId(rcaTicket.getParentTicketId());
                            }else {
                                tmpRcaTicket.setTicketId(rcaTicket.getTicketId());
                            }

                            properties = new HashMap<String, String>();
                            properties.put("status", RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                            parameterMap = new HashMap<String, String>();
                            parameterMap.put("ticketId", tmpRcaTicket.getTicketId());
                            parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_FIN);

                            rcaEngineResult = rcaEngineResultFactory.getObject();
                            rcaEngineResult.setTicketId(tmpRcaTicket.getTicketId());
                            rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);
                            rcaEngineResult.setProperties(properties);
                            rcaTicketManager.uiSendTicketResult(rcaEngineResult);

                        }

                        if(clearTicketResultVo.getClearAlarmTicket() != null){
                            LOGGER.info(">>>>>>>>>EngineClearSchedulerJobServiceImpl ClearTicket : " + clearTicketResultVo.getClearAlarmTicket() +"<<<<<<<<<<<<<<");
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[EngineClearSchedulerJobServiceImpl] clearTicketCheck() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    public void clearTicket(){
        try {
            LOGGER.info(">>>>>>>>> clearTicketCheck <<<<<<<<<<<<<<");
            RcaEngineResult rcaEngineResult;
            RCATicket tmpRcaTicket;
            RCATicket rcaTicket;
            String clearTicketId;
            ClearTicketResultVo clearTicketResultVo;
            List<String> clearTicketCheckList;
            RCATicketHandlingStatus handlingStatus;
            String rootCauseType;
            String dtlCode;
            List<String> topasticketNoList;
            List<Map<String, String>> data = new ArrayList<>();

            RCATicketHandlingStatus rcaTicketHandlingStatus;

            clearTicketCheckList = ticketService.selectClearTicketCheckList();

            if (clearTicketCheckList != null && clearTicketCheckList.size() > 0){
                for (String ticketId : clearTicketCheckList) {

                    clearTicketId = ticketService.fcClearTicket(ticketId);

                    rcaTicket = ticketService.selectRcaTicket(ticketId);

                    Thread.sleep(100);

                    if(clearTicketId != null){
                        clearTicketResultVo = ticketService.fcClearTicketCheck(clearTicketId);

                        if(clearTicketResultVo != null){
                            if(clearTicketResultVo.getisAllClearTicket()){
                                LOGGER.info(">>>>>>>>>clearTicketCheck allClearAlarm : " + rcaTicket.getTicketId() +"<<<<<<<<<<<<<<");
                                rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
                                rcaTicketHandlingStatus.setTicketId(rcaTicket.getTicketId());

                                rcaTicketHandlingStatus.setStatus(RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                                if(rcaTicket.getParentTicketId() != null){
                                    rcaTicketHandlingStatus.setTicketId(rcaTicket.getParentTicketId());
                                }

                                tmpRcaTicket = rcaTicketFactory.getObject();

                                if(rcaTicket.getParentTicketId() != null){
                                    tmpRcaTicket.setTicketId(rcaTicket.getParentTicketId());
                                }else {
                                    tmpRcaTicket.setTicketId(rcaTicket.getTicketId());
                                }

                                properties = new HashMap<String, String>();
                                properties.put("status", RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                                parameterMap = new HashMap<String, String>();
                                parameterMap.put("ticketId", tmpRcaTicket.getTicketId());
                                parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_FIN);

                                rcaEngineResult = rcaEngineResultFactory.getObject();
                                rcaEngineResult.setTicketId(tmpRcaTicket.getTicketId());
                                rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_UPDATE);
                                rcaEngineResult.setProperties(properties);
                                rcaTicketManager.uiSendTicketResult(rcaEngineResult);
                            }

                            if(clearTicketResultVo.getClearAlarmTicket() != null){
                                LOGGER.info(">>>>>>>>>EngineClearSchedulerJobServiceImpl ClearTicket : " + clearTicketResultVo.getClearAlarmTicket() +"<<<<<<<<<<<<<<");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[EngineClearSchedulerJobServiceImpl] clearTicketCheck() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }
}