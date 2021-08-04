package com.nia.engine.service.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.*;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("RcaTicketMergeService")
public class RcaTicketMergeServiceImpl implements RcaTicketMergeService {
    private final Logger LOGGER = Logger.getLogger(RcaTicketMergeServiceImpl.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicketHandlingStatus> rcaTicketHandlingStatusFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RcaTicketResult> rcaTicketResultFactory;

    @Autowired
    @Qualifier("RcaTicketHandlingService")
    private RcaTicketHandlingService rcaTicketHandlingService;

    @Autowired
    private DataShareBean dataShareBean;

    private HashMap<String, String> parameterMap;

    /*
     * Ticket 중복 체크
     */
    @Override
    public synchronized RcaTicketResult rcaTicketMerge(RCATicket rcaTicket){
        LOGGER.info(">>>>>>>>>>[RcaTicketMergeServiceImpl] rcaTicketMerge rcaTicket = " + rcaTicket + " <<<<<<<<<<<<<<<<<");
        BasicAlarmVo tmpAlarmA;
        BasicAlarmVo tmpAlarmZ;
        BasicAlarmVo alarmA;
        BasicAlarmVo alarmZ;
        Iterator<RCATicket> itr;
        RCATicket tmpRcaTicket;
        RcaTicketResult rcaTicketResult = null;
        RCATicketHandlingStatus rcaTicketHandlingStatus;
        String ticketGenerationTime;
        String tmptTicketGenerationTime;
        String curDate;
        boolean isContinue = true;
        BasicAlarmVo tmpAlarm = null;
        BasicAlarmVo alarm = null;

        try{
            parameterMap = new HashMap<String, String>();
            parameterMap.put("factor","ticket_merge_time");

            if(((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0) {
                itr = ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();
                curDate = UtlDateHelper.getCurrentDate();
                while (itr.hasNext()) {
                    tmpRcaTicket = itr.next();
                    tmptTicketGenerationTime = UtlDateHelper.timestampToString("yyyy-MM-dd", tmpRcaTicket.getTicketGenerationTime());
                    if (tmptTicketGenerationTime.equals(curDate) && tmpRcaTicket.getParentTicketId() == null) {
                        if (!tmpRcaTicket.getTicketId().equals(rcaTicket.getTicketId())) {
                            if (!RcaCodeInfo.TICKET_STATUS_FIN.equals(tmpRcaTicket.getStatus())) {
                                ticketGenerationTime = UtlDateHelper.timestampToString("yyyy-MM-dd", rcaTicket.getTicketGenerationTime());
                                if (ticketGenerationTime.equals(curDate)) {
                                    //if(diffHours<=Integer.parseInt(((mergeTime != null)?mergeTime:"0"))+{
                                    if (tmpRcaTicket != null && tmpRcaTicket.getTicketAlList() != null) {
                                        for (RCATicketAl tmpRcaTicketAl : tmpRcaTicket.getTicketAlList()) {
                                            tmpAlarmA = tmpRcaTicketAl.getRootCauseAlarmInfoA();
                                            tmpAlarmZ = tmpRcaTicketAl.getRootCauseAlarmInfoZ();
                                            for (RCATicketAl rcaTicketAl : rcaTicket.getTicketAlList()) {
                                                alarmA = rcaTicketAl.getRootCauseAlarmInfoA();
                                                alarmZ = rcaTicketAl.getRootCauseAlarmInfoZ();

                                                if (tmpAlarmA == null && tmpAlarmZ != null) {
                                                    tmpAlarm = tmpAlarmZ;
                                                } else {
                                                    tmpAlarm = tmpAlarmA;
                                                }
                                                if (alarmA == null && alarmZ != null) {
                                                    alarm = alarmZ;
                                                } else {
                                                    alarm = alarmA;
                                                }

                                                if (isContinue) {
                                                    if ((tmpAlarm != null && alarm != null)) {
                                                        if(tmpAlarm.getSysname().equals(alarm.getSysname()) && tmpAlarm.getPtpname().equals(alarm.getPtpname())) {
                                                            rcaTicketResult = rcaTicketResultFactory.getObject();
                                                            rcaTicketResult.setTicketId(tmpRcaTicket.getTicketId());
                                                            rcaTicketResult.setValue(tmpRcaTicket.getStatus());
                                                            rcaTicketResult.setResult(true);

                                                            rcaTicket.setParentTicketId(tmpRcaTicket.getTicketId());

                                                            if (RcaCodeInfo.TICKET_STATUS_AUTO_FIN.equals(tmpRcaTicket.getStatus())) {
                                                                rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
                                                                rcaTicketHandlingStatus.setTicketId(tmpRcaTicket.getTicketId());
                                                                rcaTicketHandlingStatus.setStatus(RcaCodeInfo.TICKET_STATUS_AUTO_FIN);
                                                                rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);
                                                            }

                                                            LOGGER.info(">>>>>>>>>>[RcaTicketMergeServiceImpl] rcaTicketMerge Sysname : parent = " + rcaTicket.getParentTicketId() + ", child = " + rcaTicket.getTicketId() + " <<<<<<<<<<<<<<<<<");
                                                            return rcaTicketResult;
                                                        }else if((tmpAlarm.getTopology() != null && alarm.getTopology() != null)) {
                                                            if (tmpAlarm.getTopology().getTrunkId().equals(alarm.getTopology().getTrunkId())) {
                                                                rcaTicketResult = rcaTicketResultFactory.getObject();
                                                                rcaTicketResult.setTicketId(tmpRcaTicket.getTicketId());
                                                                rcaTicketResult.setValue(tmpRcaTicket.getStatus());
                                                                rcaTicketResult.setResult(true);

                                                                rcaTicket.setParentTicketId(tmpRcaTicket.getTicketId());

                                                                if (RcaCodeInfo.TICKET_STATUS_AUTO_FIN.equals(tmpRcaTicket.getStatus())) {
                                                                    rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
                                                                    rcaTicketHandlingStatus.setTicketId(tmpRcaTicket.getTicketId());
                                                                    rcaTicketHandlingStatus.setStatus(RcaCodeInfo.TICKET_STATUS_AUTO_FIN);
                                                                    rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);
                                                                }

                                                                LOGGER.info(">>>>>>>>>>[RcaTicketMergeServiceImpl] rcaTicketMerge TrunkId : parent = " + rcaTicket.getParentTicketId() + ", child = " + rcaTicket.getTicketId() + " <<<<<<<<<<<<<<<<<");
                                                                return rcaTicketResult;
                                                            }
                                                            else if (tmpAlarm.getTopology().getLinkId().equals(alarm.getTopology().getLinkId())) {
                                                                rcaTicketResult = rcaTicketResultFactory.getObject();
                                                                rcaTicketResult.setTicketId(tmpRcaTicket.getTicketId());
                                                                rcaTicketResult.setValue(tmpRcaTicket.getStatus());
                                                                rcaTicketResult.setResult(true);

                                                                rcaTicket.setParentTicketId(tmpRcaTicket.getTicketId());

                                                                if (RcaCodeInfo.TICKET_STATUS_AUTO_FIN.equals(tmpRcaTicket.getStatus())) {
                                                                    rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
                                                                    rcaTicketHandlingStatus.setTicketId(tmpRcaTicket.getTicketId());
                                                                    rcaTicketHandlingStatus.setStatus(RcaCodeInfo.TICKET_STATUS_AUTO_FIN);
                                                                    rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);
                                                                }

                                                                LOGGER.info(">>>>>>>>>>[RcaTicketMergeServiceImpl] rcaTicketMerge LinkId : parent = " + rcaTicket.getParentTicketId() + ", child = " + rcaTicket.getTicketId() + " <<<<<<<<<<<<<<<<<");
                                                                return rcaTicketResult;
                                                            }
                                                        }
                                                    }
                                                    isContinue = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(rcaTicketResult == null){
                rcaTicketResult = rcaTicketResultFactory.getObject();
                rcaTicketResult.setResult(false);
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTicketMergeServiceImpl] rcaTicketMerge error("+rcaTicket.getTicketId()+") : "+ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<");
            return rcaTicketResult;
        }
        return rcaTicketResult;
    }
}
