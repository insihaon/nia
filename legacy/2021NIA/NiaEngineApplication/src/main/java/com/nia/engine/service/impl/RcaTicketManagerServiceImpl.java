package com.nia.engine.service.impl;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.mapper.TopologyMapper;
import com.nia.engine.service.*;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("RcaTicketManagerService")
public class RcaTicketManagerServiceImpl implements RcaTicketManagerService {
    private final Logger LOGGER = Logger.getLogger(RcaTicketManagerServiceImpl.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicket> rcaTicketFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicketAl> rcaTicketAlFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCAHandlingStatus> rcaHandlingStatusObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RcaEngineResult> rcaEngineResultFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TicketCableVo> ticketCableVoObjectFactory;

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Autowired
    @Qualifier("TopologyService")
    private TopologyService topologyService;

    @Autowired
    @Qualifier("PerformanceTicketService")
    private PerformanceTicketService performanceTicketService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Autowired
    @Qualifier("RcaTicketMergeService")
    private RcaTicketMergeService rcaTicketMergeService;

    @Autowired
    @Qualifier("RcaTicketProfileService")
    private RcaTicketProfileService rcaTicketProfileService;

    @Autowired
    private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    @Autowired
    private DataShareBean dataShareBean;

    private HashMap<String, String> parameterMap;

    private Map<String, String> properties;

    @Autowired
    private RCATicket rcaTicket;

    @Value("${spring.profiles}")
    private String profiles;

    @Override
    public void createPerformanceTicket(PerformanceClusterVo performanceClusterVo) {
        LOGGER.info("==========>[RcaTicketManager] createPerformanceTicket performanceClusterVo : " + performanceClusterVo + "<==============");
        TopologyDataVo topologyDataVo;
        HashMap<String, String> parameterMap = null;
        List<List<RoadmPerformanceOrgVo>> rowPerformanaceList;
        TicketCableVo ticketCableVo;
        List<TicketCableVo> ticketCableList;
        RcaEngineResult rcaEngineResult;
        String ticketId = "";

        try {
            if(performanceClusterVo.getRoadmPerformanceList() != null && performanceClusterVo.getRoadmPerformanceList().size() > 0){
                rowPerformanaceList = new ArrayList<List<RoadmPerformanceOrgVo>>();

                if("UP".equals(performanceClusterVo.getDirection())){
                    Collections.sort(performanceClusterVo.getRoadmPerformanceList(), new RoutenumDescendingPerformanaceData());
                    rowPerformanaceList.add(performanceClusterVo.getRoadmPerformanceList());
                }

                if("DOWN".equals(performanceClusterVo.getDirection())){
                    Collections.sort(performanceClusterVo.getRoadmPerformanceList(), new RoutenumAscendingPerformanaceData());
                    rowPerformanaceList.add(performanceClusterVo.getRoadmPerformanceList());
                }

                if(rowPerformanaceList != null && rowPerformanaceList.size() > 0){
                    ticketCableList = new ArrayList<TicketCableVo>();

                    ticketId = ticketService.selectTicketKey();

                    rcaTicket = rcaTicketFactory.getObject();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setClusterNo(null);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_PERFORMACE);
                    rcaTicket.setTicketRcaResultCode("성능 장애");
                    rcaTicket.setTicketRcaResultDtlCode("성능 장애");
                    rcaTicket.setFaultTime(rowPerformanaceList.get(0).get(0).getOcrtime());
                    rcaTicket.setRootCauseCode(null);
                    rcaTicket.setRootCauseDomain("ROADM");
                    rcaTicket.setRootCauseType("Performance");
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                    rcaTicket.setOccur(true);
                    rcaTicket.setDirection(performanceClusterVo.getDirection());
                    LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");

                    for(List<RoadmPerformanceOrgVo> performanaceVoList : rowPerformanaceList){
                        if(performanaceVoList.size() > 0){
                            for(int i = 0; i < performanaceVoList.size(); i++){ //전송망 오류 발생으로 인해 <= 를 < 로 변경
                                if(i == 0){
                                    if("REPEATER".equals(performanaceVoList.get(i).getRoadmCode())){
                                        if(performanaceVoList.get(i).getIsInRowSignal()){
                                            parameterMap = new HashMap<String, String>();
                                            parameterMap.put("sysname", performanaceVoList.get(i).getSysname().split("-")[0]);

                                            if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                                parameterMap.put("ptpNameBau", performanaceVoList.get(i).getSysname().split("-")[1]+"-"+performanaceVoList.get(i).getPort());
                                            }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                                parameterMap.put("ptpNamePau", performanaceVoList.get(i).getSysname().split("-")[1]+"-"+performanaceVoList.get(i).getPort());
                                            }
                                            topologyDataVo = topologyService.selectTopology(parameterMap);

                                            if(topologyDataVo != null){
                                                ticketCableVo = ticketCableVoObjectFactory.getObject();
                                                ticketCableVo.setTicketId(ticketId);

                                                if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                                    ticketCableVo.setSysnamea(topologyDataVo.getSysnamea());
                                                    ticketCableVo.setPorta(topologyDataVo.getPtpnameaBau());
                                                    ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                                    ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                                }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                                    ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                                    ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                                    ticketCableVo.setSysnamez(topologyDataVo.getSysnamez());
                                                    ticketCableVo.setPortz(topologyDataVo.getPtpnamezBau());
                                                }
                                                ticketCableVo.setIsRootRowSignalLine(true);

                                                ticketCableList.add(ticketCableVo);
                                            }
                                        }else if(performanaceVoList.get(i).getIsOutRowSignal()){
                                            ticketCableVo = ticketCableVoObjectFactory.getObject();
                                            ticketCableVo.setTicketId(ticketId);

                                            if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                                ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                                ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                                ticketCableVo.setSysnamez(performanaceVoList.get(i+1).getSysname());
                                                ticketCableVo.setPortz(performanaceVoList.get(i+1).getPort());
                                            }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                                ticketCableVo.setSysnamea(performanaceVoList.get(i+1).getSysname());
                                                ticketCableVo.setPorta(performanaceVoList.get(i+1).getPort());
                                                ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                                ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                            }
                                            ticketCableVo.setIsRootRowSignalLine(true);

                                            ticketCableList.add(ticketCableVo);
                                        }
                                    }else{
                                        if(i == performanaceVoList.size() -1){
                                            LOGGER.info("==========>[RcaTicketManager] REPEATER가 아닌데 size가 1 이면 반드시 오류가 나므로 return 시킵니다");
                                            return;
                                        }

                                        ticketCableVo = ticketCableVoObjectFactory.getObject();
                                        ticketCableVo.setTicketId(ticketId);

                                        if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                            ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                            ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                            ticketCableVo.setSysnamez(performanaceVoList.get(i+1).getSysname());
                                            ticketCableVo.setPortz(performanaceVoList.get(i+1).getPort());
                                        }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                            ticketCableVo.setSysnamea(performanaceVoList.get(i+1).getSysname());
                                            ticketCableVo.setPorta(performanaceVoList.get(i+1).getPort());
                                            ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                            ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                        }
                                        ticketCableVo.setIsRootRowSignalLine(true);

                                        ticketCableList.add(ticketCableVo);
                                    }
                                }else{
                                    if(i == performanaceVoList.size() -1){
                                        break;
                                    }
                                    ticketCableVo = ticketCableVoObjectFactory.getObject();
                                    ticketCableVo.setTicketId(ticketId);

                                    if("DOWN".equals(performanaceVoList.get(i).getDirection())){
                                        ticketCableVo.setSysnamea(performanaceVoList.get(i).getSysname());
                                        ticketCableVo.setPorta(performanaceVoList.get(i).getPort());
                                        ticketCableVo.setSysnamez(performanaceVoList.get(i+1).getSysname());
                                        ticketCableVo.setPortz(performanaceVoList.get(i+1).getPort());
                                    }else if("UP".equals(performanaceVoList.get(i).getDirection())){
                                        ticketCableVo.setSysnamea(performanaceVoList.get(i+1).getSysname());
                                        ticketCableVo.setPorta(performanaceVoList.get(i+1).getPort());
                                        ticketCableVo.setSysnamez(performanaceVoList.get(i).getSysname());
                                        ticketCableVo.setPortz(performanaceVoList.get(i).getPort());
                                    }

                                    ticketCableList.add(ticketCableVo);
                                }
                            }
                        }
                    }

                    ticketService.insertRcaTicket(rcaTicket);

//                    for(TicketCableVo ticketCableVo1 : ticketCableList) {
//                        LOGGER.info("==========>[RcaTicketManager] createPerformanceTicket ticketCableVo : " + ticketCableVo1 + "<==============");
//                        performanceTicketService.insertPerformanceTicket(ticketCableVo1);
//
//                        rcaEngineResult = new RcaEngineResult();
//                        rcaEngineResult.setTicketId(ticketCableVo1.getTicketId());
//                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
//                        rcaEngineResult.setTicketType("PF");
//
//                        uiSendTicketResult(rcaEngineResult);
//                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[TicketService] createTicket() error : " + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createRcaTicket(List<RcaResult> rcaResultList){
        // RT 티켓 발행          
        LOGGER.info("==========>[RcaTicketManager] createRcaTicket : "+rcaResultList+"<==============");
        RCATicket rcaTicket;
        String ticketId;
        List<RCATicketAl> rcaTicketAlList= null;
        Iterator<RcaResult> itr;
        RcaResult rcaResult;
        List<BasicAlarmVo> basicAlarmVoList= null;
        RCATicketAl rcaTicketAl;
        RCAHandlingStatus rcaTicketStatus;
        RcaEngineResult rcaEngineResult;
        RcaTicketResult rcaTicketResult;
        BasicAlarmVo basicAlarmVo;
        String updateTime;
        HashMap<String, String> ticketUpdateTime = new HashMap<String, String>();

        try{
            if(rcaResultList != null && rcaResultList.size() > 0){
                itr = rcaResultList.iterator();

                while( itr.hasNext() ) {
                    rcaResult = itr.next();

//                    String time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", rcaResult.getAlarmTime()) + " 00:00:00";
//                    String time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", rcaResult.getAlarmTime()) + " 06:00:00";
//                    Timestamp time3 = UtlDateHelper.stringToTimestamp(time1);
//                    Timestamp time4 = UtlDateHelper.stringToTimestamp(time2);

//                    if (RcaCodeInfo.DOMAIN_ROADM.equals(rcaResult.getDomainCode()) || RcaCodeInfo.DOMAIN_POTN.equals(rcaResult.getDomainCode())) {
//                        if (rcaResult.getAlarmTime().getTime() > time4.getTime()) {
                            basicAlarmVoList = rcaResult.getRelatedAlarmList();
                            ticketId = ticketService.selectTicketKey();
                            rcaTicket = rcaTicketFactory.getObject();
                            rcaTicket.setTicketId(ticketId);
                            rcaTicket.setClusterNo(rcaResult.getClusterNo());
                            rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_RCATICKET);
                            rcaTicket.setTicketRcaResultCode(rcaResult.getRcaResultCode());
                            rcaTicket.setTicketRcaResultDtlCode(rcaResult.getFaultDetailCode());
                            rcaTicket.setTicketRcaResultOrigDtlCode(rcaResult.getFaultDetailCode());
                            rcaTicket.setTicketRcaResultCode(rcaResult.getRcaResultCode());
                            rcaTicket.setFaultTime(rcaResult.getAlarmTime());
                            rcaTicket.setRootCauseCode(rcaResult.getNwType());
                            rcaTicket.setRootCauseDomain(rcaResult.getDomainCode());
                            rcaTicket.setRootCauseType(rcaResult.getRcaResultTypeCode());
                            rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                            rcaTicket.setOccur(true);

                            if (basicAlarmVoList != null) {
                                rcaTicketAlList = new ArrayList<RCATicketAl>();

                                for (BasicAlarmVo basicAlarm : basicAlarmVoList) {
                                    rcaTicketAl = rcaTicketAlFactory.getObject();

                                    basicAlarmVo = alarmService.selectAlarmInfo(basicAlarm.getAlarmno());
                                    rcaTicketAl.setRootCauseAlarmInfoA(basicAlarmVo);

                                    rcaTicketAl.setTicketId(ticketId);
                                    rcaTicketAl.setRootCauseAlarmLevelA(basicAlarm.getAlarmlevel());
                                    rcaTicketAl.setRootCauseSysnameA(basicAlarm.getSysname());
                                    rcaTicketAl.setRootCauseEquipTypeA(basicAlarm.getEquiptype());
                                    rcaTicketAl.setRootCauseUnitA(basicAlarm.getUnit());
                                    rcaTicketAl.setRootCausePtpnameA(basicAlarm.getPtpname());
                                    rcaTicketAl.setRootCauseSlotA(basicAlarm.getSlot());
                                    rcaTicketAl.setRootCauseAlarmNoA(basicAlarm.getAlarmno());
                                    rcaTicketAl.setRootCausePortA(basicAlarm.getAlarmloc());

                                    if(basicAlarm.getTopology() != null) {
                                        if("ROADM".equals(basicAlarm.getEquiptype())) {
                                            rcaTicketAl.setRootCauseSysnameZ(basicAlarm.getTopology().getOppSysname() + "-SH1");
                                        } else{
                                            rcaTicketAl.setRootCauseSysnameZ(basicAlarm.getTopology().getOppSysname());
                                        }
                                        rcaTicketAl.setRootCausePtpnameZ(basicAlarm.getTopology().getOppPtpName());
                                        rcaTicketAl.setRootCauseSlotZ(basicAlarm.getTopology().getOppSlot());
                                        rcaTicketAl.setRootCauseEquipTypeZ(basicAlarm.getEquiptype());
                                        rcaTicketAl.setRootCausePortA(basicAlarm.getTopology().getOppPort());
                                    }

                                    rcaTicketAlList.add(rcaTicketAl);
                                }
                                rcaTicket.setRelatedAlarmCnt(rcaResult.getRelatedAlarmList().size());
                            }

                            if (rcaTicketAlList != null) {
                                rcaTicket.setTicketAlList(rcaTicketAlList);
                            }
                            rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                            rcaTicketResult = rcaTicketMergeService.rcaTicketMerge(rcaTicket);

                            LOGGER.info("==========>[RcaTicketManager] rcaTicketResult : " + rcaTicketResult + "<==============");
                            rcaTicketStatus = rcaHandlingStatusObjectFactory.getObject();
                            rcaTicketStatus.setTicketId(rcaTicket.getTicketId());
                            rcaTicketStatus.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                            LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                            LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                            ticketService.insertRcaTicket(rcaTicket);
                            ticketService.insertRcaTicketAl(rcaTicketAlList);

                            rcaTicketStatus = rcaHandlingStatusObjectFactory.getObject();
                            rcaTicketStatus.setTicketId(rcaTicket.getTicketId());
                            rcaTicketStatus.setHandlingUser("AI");
                            rcaTicketStatus.setAutoMation("Y");
                            rcaTicketStatus.setHandlingTime(UtlDateHelper.getCurrentTime());
                            rcaTicketStatus.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                            rcaTicketStatus.setTicketType("RT");
                            rcaTicketStatus.setTicketGenerationTime(rcaTicket.getTicketGenerationTime());

//                            ticketService.updateRCATicketHandlingStatus(rcaTicketStatus);
                            ticketService.insertRCATicketHandlingStatus(rcaTicketStatus);
                            ticketService.insertRCATicketHandlingStatusHist(rcaTicketStatus);
                            //handling 테이블에 저장.

                            if (!rcaTicketResult.isResult()) {
                                updateTime = String.valueOf(rcaTicket.getTicketGenerationTime());

                                if (rcaTicket.getParentTicketId() != null) {
                                    ticketService.updateRcaTicketChild(rcaTicket);
                                }

                                ticketUpdateTime = new HashMap<String, String>();
                                ticketUpdateTime.put("ticketId", rcaTicket.getParentTicketId());
                                ticketUpdateTime.put("ticketUpdateTime", updateTime);

                                ticketService.updateRcaTicketUpdateTime(ticketUpdateTime);

                                ticketService.insertRcaTicketCnt(rcaTicket);

                                rcaEngineResult = new RcaEngineResult();
                                rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                                rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                                rcaEngineResult.setTicketType("RT");

                                uiSendTicketResult(rcaEngineResult);

                                parameterMap = new HashMap<String, String>();
                                parameterMap.put("ticketId", rcaEngineResult.getTicketId());
                                parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_INIT);

                                // singletoneEngineData.addTicket(rcaTicket);
                                ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).add(rcaTicket);
                            } else {
                                properties = new HashMap<String, String>();

                                if (rcaTicket.getParentTicketId() != null) {
                                    ticketService.updateRcaTicketChild(rcaTicket);
                                    rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                                    rcaTicket.addChild();
                                }

                                updateTime = String.valueOf(rcaTicket.getTicketGenerationTime());
                                ticketUpdateTime = new HashMap<String, String>();
                                ticketUpdateTime.put("ticketId", rcaTicket.getParentTicketId());
                                ticketUpdateTime.put("ticketUpdateTime", updateTime);

                                ticketUpdateTime = new HashMap<String, String>();
                                ticketUpdateTime.put("ticketId", rcaTicket.getParentTicketId());
                                ticketUpdateTime.put("ticketUpdateTime", updateTime);

                                ticketService.updateRcaTicketUpdateTime(ticketUpdateTime);

                                properties.put("ticket_update_time", updateTime);

                                rcaEngineResult = rcaEngineResultFactory.getObject();
                                rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                                rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);

                                ticketService.insertRcaTicketCnt(rcaTicket);

                                uiSendTicketResult(rcaEngineResult);

                                if (rcaTicket.getParentTicketId() != null) {
                                    parameterMap = new HashMap<String, String>();
                                    parameterMap.put("ticketId", rcaTicket.getTicketId());
                                    parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_FIN);

                                    parameterMap = new HashMap<String, String>();
                                    parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                                    parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_INIT);
                                }
                            }

                            rcaTicketProfileService.profileCheck(rcaTicket);
                            ticketClearCheck(rcaTicket);
//                        }
                    }
                }
//            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTicketManager] createTicket() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void ticketClearCheck(RCATicket rcaTicket){
        try{
            ((ConcurrentLinkedQueue<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_TICKET_LIST)).add(rcaTicket);
        }catch (Exception e){
            LOGGER.error("=====> [RcaTicketManager] ticketClearCheak error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void uiSendTicketResult(RcaEngineResult rcaEngineResult){
        try{
            engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>RcaTicketManager uiSendTicketResult error : "+ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<");
        }
    }

    @Override
    public RCATicket getParentTicket(String ticketId){
        Iterator<RCATicket> itr;
        RCATicket rcaTicket = null;

        if(((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0){
            itr = ((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();
            while( itr.hasNext() ) {
                rcaTicket = itr.next();
                if(ticketId.equals(rcaTicket.getTicketId())){
                    break;
                }
            }
        }
        return rcaTicket;
    }
}
