package com.nia.engine.service.impl;

import com.nia.engine.amqp.NiaEngineToAiAnoAmqp;
import com.nia.engine.amqp.NiaEngineToAiNoxAmqp;
import com.nia.engine.common.CommonProperties;
import com.nia.engine.common.Data;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.mapper.*;
import com.nia.engine.service.*;
import com.nia.engine.vo.*;
import com.nia.engine.vo.aiTraffic.EngineNttTrafficResultVo;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrfficVo;
import com.nia.engine.vo.engineToAi.EngineToAiAnoVo;
import com.nia.engine.vo.engineToAi.EngineToAiNoxVo;
import com.nia.engine.vo.sdn.factor.NodeFactorInfoVo;
import com.nia.engine.vo.sdn.factor.NodeFactorListVo;
import com.nia.engine.vo.sdn.factor.NodeFactorVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficInfoVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficListVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficVo;
import com.nia.engine.vo.selfProcess.AutoTreatProcessVo;
import com.nia.engine.vo.syslog.SyslogListVo;
import com.nia.engine.vo.syslog.SyslogVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service("RcaTrafficTicketService")
public class RcaTrafficTicketServiceImpl implements RcaTrafficTicketService {
    private final Logger LOGGER = Logger.getLogger(RcaTrafficTicketServiceImpl.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicket> rcaTicketFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicketAl> rcaTicketAlFactory;

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Autowired
    @Qualifier("TopologyService")
    private TopologyService topologyService;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private AutoProcessMapper autoProcessMapper;

    @Autowired
    @Qualifier("RcaTicketManagerService")
    private RcaTicketManagerService rcaTicketManagerService;

    @Autowired(required = false)
    @Qualifier("RcaMailingService")
    private RcaMailingService rcaMailingService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCAHandlingStatus> rcaHandlingStatusObjectFactory;

    @Autowired
    @Qualifier("RcaTicketMergeService")
    private RcaTicketMergeService rcaTicketMergeService;

    @Autowired
    private TopologyMapper topologyMapper;

    @Autowired
    private NodeFactorMapper nodeFactorMapper;

    @Autowired
    private SdnTrafficMapper sdnTrafficMapper;

    @Autowired
    private RCATicket rcaTicket;

    @Value("${spring.profiles}")
    private String profiles;

    @Autowired
    private NiaEngineToAiAnoAmqp niaEngineToAiAnoAmqp;

    @Autowired
    private NiaEngineToAiNoxAmqp niaEngineToAiNoxAmqp;

    @Override
    public void createAnomalousTrafficTicket(AnomalousTrafficListVo anomalousTrafficListVo){
        LOGGER.info("==========>[RcaTrafficTicketService] createAnomalousTrafficTicket : " + anomalousTrafficListVo + " <==============");
        Map<String, String> properties;
        int updateCnt = 0;

        String ticketId;
        Timestamp faultTime;
        AnomalousTrafficVo anomalousTrafficVo;
        AnomalousTrafficVo tmpAnomalousTrafficVo;
        RcaEngineResult rcaEngineResult;
        RCATicketAl rcaTicketAl;
        UserOrganVo userOrganVo;
        BackboneLinkVo backboneLinkVo;
        RCAHandlingStatus rcaTicketStatus;

        Iterator<AnomalousTrafficVo> itr;
        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;
        HashMap<String, String> autoProcessInserMap;

        try{
            if(anomalousTrafficListVo != null && anomalousTrafficListVo.getData().size() > 0){
                itr = anomalousTrafficListVo.getData().iterator();

                while( itr.hasNext() ) {
                    anomalousTrafficVo = itr.next();

                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("strifid", anomalousTrafficVo.getStrifid());
                    parameterMap.put("strresid", anomalousTrafficVo.getStrresid());
                    parameterMap.put("inttimestamp", anomalousTrafficVo.getInttimestamp() + "");

                    tmpAnomalousTrafficVo = ticketService.selectAnomalousTrafficAlarm(parameterMap);

                    if(tmpAnomalousTrafficVo != null) {
                        anomalousTrafficVo.setIfId(tmpAnomalousTrafficVo.getIfId());
                        anomalousTrafficVo.setStrresid(tmpAnomalousTrafficVo.getStrresid());
                        anomalousTrafficVo.setNodeId(tmpAnomalousTrafficVo.getNodeId());
                    }

                    faultTime  = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(anomalousTrafficVo.getInttimestamp() * 1000L)));

                    rcaTicket = rcaTicketFactory.getObject();
                    ticketId = ticketService.selectTicketKey();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_ANOMALOUS_TRAFFIC_TICKET);
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setFaultTime(faultTime);
                    rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_TRAFFIC_FAIL);
                    rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_TRAFFIC);
                    rcaTicket.setOccur(true);

                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                    rcaTicket.setRelatedAlarmCnt(1);

                    rcaTicketAl = rcaTicketAlFactory.getObject();
                    rcaTicketAlList = new ArrayList<>();

                    rcaTicketAl.setRootCauseAnomalousTrafficAlarmInfoA(anomalousTrafficVo);
                    rcaTicketAl.setTicketId(ticketId);
                    rcaTicketAl.setRootCauseSysnameA(anomalousTrafficVo.getNodeId());
                    rcaTicketAl.setRootCausePortA(anomalousTrafficVo.getIfId());
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("resid", anomalousTrafficVo.getStrresid());
                    parameterMap.put("ifid", anomalousTrafficVo.getStrifid());
                    backboneLinkVo = topologyService.selectBackboneTopology(parameterMap);

                    if(backboneLinkVo != null){
                        rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(링크)");

                        if(anomalousTrafficVo.getStrresid().equals(backboneLinkVo.getSrcNodeNum())){
                            rcaTicketAl.setRootCauseSysnameZ(backboneLinkVo.getDestNodeId());
                            rcaTicketAl.setRootCausePortZ(backboneLinkVo.getDestIfId());
                        }else{
                            rcaTicketAl.setRootCauseSysnameZ(backboneLinkVo.getSrcNodeId());
                            rcaTicketAl.setRootCausePortZ(backboneLinkVo.getSrcIfId());
                        }
                    }else{
                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("nodeId", anomalousTrafficVo.getNodeId());
                        parameterMap.put("ifId", anomalousTrafficVo.getIfId());
                        userOrganVo = ticketService.selectUserOrgan(parameterMap);

                        if(userOrganVo != null){
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(이용기관)");

                            rcaTicketAl.setRootCauseSysnameZ(userOrganVo.getNrenName());
                            rcaTicketAl.setRootCausePortZ(userOrganVo.getIfId());
                        }else{
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(포트)");
                            rcaTicketAl.setRootCauseSysnameZ("Unknown");
                        }
                    }

                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("ticketId", ticketId);
                    parameterMap.put("strifid", anomalousTrafficVo.getStrifid());
                    parameterMap.put("strresid", anomalousTrafficVo.getStrresid());
                    parameterMap.put("inttimestamp", anomalousTrafficVo.getInttimestamp() + "");

                    ticketService.updateAnomalousTrafficTicketId(parameterMap);

                    rcaTicketAlList.add(rcaTicketAl);

                    if (rcaTicketAlList != null) {
                        rcaTicket.setTicketAlList(rcaTicketAlList);
                    }
                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                    RcaTicketResult rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);
                    if(rcaTicketResult != null && rcaTicketResult.isResult()){
                        rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                        rcaTicket.setStatus(rcaTicketResult.getValue());
                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);
                        rcaEngineResult.setTicketType("RT");

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                        parameterMap.put("ticketUpdateTime", rcaTicket.getTicketGenerationTime()+"");
                        ticketService.updateRcaTicketUpdateTime(parameterMap);
                    }else{
                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                        rcaEngineResult.setTicketType("RT");
                    }

                    LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                    ticketService.insertRcaTicket(rcaTicket);
                    LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                    ticketService.insertRcaTicketAl(rcaTicketAlList);

                    if(StringUtils.isNotEmpty(rcaTicket.getParentTicketId())){
                        ticketService.updateTicketCnt(rcaTicket);

                        updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());

                        properties = new HashMap<String, String>();
                        properties.put("child_count", updateCnt + "");
                        properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime()+"");

                        rcaEngineResult.setProperties(properties);
                    }

                    ticketService.insertRcaTicketCnt(rcaTicket);

                    rcaTicketStatus = rcaHandlingStatusObjectFactory.getObject();
                    rcaTicketStatus.setTicketId(rcaTicket.getTicketId());
                    rcaTicketStatus.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                    rcaTicketStatus = rcaHandlingStatusObjectFactory.getObject();
                    rcaTicketStatus.setTicketId(rcaTicket.getTicketId());
                    rcaTicketStatus.setHandlingUser("AI");
                    rcaTicketStatus.setAutoMation("Y");
                    rcaTicketStatus.setHandlingTime(UtlDateHelper.getCurrentTime());
                    rcaTicketStatus.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                    rcaTicketStatus.setTicketType("ATT");
                    rcaTicketStatus.setTicketGenerationTime(rcaTicket.getTicketGenerationTime());

                    ticketService.insertRCATicketHandlingStatus(rcaTicketStatus);
                    ticketService.insertRCATicketHandlingStatusHist(rcaTicketStatus);

                    rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createAnomalousTrafficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createNoxiousTrfficAiTicket(EngineNttTrafficResultVo engineNttTrafficResultVo){
        LOGGER.info("==========>[RcaTrafficTicketService] createNoxiousTrfficAiTicket : " + engineNttTrafficResultVo + " <==============");

        try{
            if(engineNttTrafficResultVo.getTraffic_type().equals("normalTraffic")){
                return;
            }

            String ticketId = ticketService.selectTicketKey();
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp faultTime = Timestamp.valueOf(currentDateTime);

            rcaTicket = rcaTicketFactory.getObject();
            rcaTicket.setTicketId(ticketId);
            rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_NOXIOUS_TRAFFIC_TICKET_AI);
            rcaTicket.setFaultTime(faultTime);
            rcaTicket.setRootCauseType(engineNttTrafficResultVo.getTraffic_type());
            rcaTicket.setRootCauseDomain(engineNttTrafficResultVo.getJsonKey());
            rcaTicket.setTicketRcaResultCode("TRAFFIC_NOXIOUS_DETECTION-TEST");
            rcaTicket.setTicketRcaResultDtlCode("유해 트래픽 탐지-TEST");
            rcaTicket.setOccur(true);
            rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
            rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());

            LOGGER.info("==========> createNoxiousTrfficAiTicket createTicket : " + rcaTicket.toString() + "<==============");
            ticketService.insertRcaTicket(rcaTicket);

            engineNttTrafficResultVo.setTicketId(ticketId);
            LOGGER.info("==========> createNoxiousTrfficAiTicket creatRcaNttTicketDetailInfo : " + engineNttTrafficResultVo.toString() + "<==============");
            ticketService.insertRcaNTTTicketDeatailInfo(engineNttTrafficResultVo);
            ticketService.insertTbSflowCollectHist(engineNttTrafficResultVo);

            HashMap<String, String> autoProcessInserMap = new HashMap<>();
            if (rcaTicket.getParentTicketId() == null){
                autoProcessInserMap.put("selfProcessGroup","SO");
                autoProcessInserMap.put("selfProcessType","N");
                autoProcessInserMap.put("occur_time", String.valueOf(rcaTicket.getTicketGenerationTime()));
                autoProcessInserMap.put("ticketId",rcaTicket.getTicketId());
                autoProcessInserMap.put("ticketType",rcaTicket.getTicketType());
                autoProcessInserMap.put("alarmno",null);

                autoProcessMapper.insertAutoProcess(autoProcessInserMap);
                LOGGER.info("self_process insert ======> "+ rcaTicket.getTicketId());
            }
        }catch(Exception e){
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createNoxiousTrfficAiTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createNoxiousTrfficTicket(NoxiousTrafficListVo noxiousTrafficListVo){
        LOGGER.info("==========>[RcaTrafficTicketService] createNoxiousTrfficTicket : " + noxiousTrafficListVo + " <==============");
        Map<String, String> properties;
        int updateCnt = 0;

        String ticketId;
        Timestamp faultTime;
        RCATicketAl rcaTicketAl;
        RcaEngineResult rcaEngineResult;
        RcaTicketResult rcaTicketResult = null;
        RCAHandlingStatus rcaTicketStatus;

        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;
        HashMap<String, String> autoProcessInserMap;
        HashMap<String, String> autoProcessSelectMap;

        EngineToAiNoxVo engineToAiNoxVo = null;

        try {
            if(noxiousTrafficListVo != null && noxiousTrafficListVo.getData().size() > 0) {
                for(NoxiousTrfficVo noxiousTrffic : noxiousTrafficListVo.getData()) {
                    faultTime  = Timestamp.valueOf(noxiousTrffic.getDateregdate());

//                    String time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
//                    String time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
//                    Timestamp time3 = UtlDateHelper.stringToTimestamp(time1);
//                    Timestamp time4 = UtlDateHelper.stringToTimestamp(time2);

//                    if(faultTime.getTime() > time4.getTime()) {
                    rcaTicket = rcaTicketFactory.getObject();
                    ticketId = ticketService.selectTicketKey();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_NOXIOUS_TRAFFIC_TICKET);
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setFaultTime(faultTime);
                    rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_TRAFFIC_FAIL);
                    rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_TRAFFIC);
                    rcaTicket.setTicketRcaResultCode("TRAFFIC_NOXIOUS_DETECTION");
                    rcaTicket.setTicketRcaResultDtlCode("유해 트래픽 탐지");
                    rcaTicket.setOccur(true);
                    rcaTicket.setRelatedAlarmCnt(noxiousTrafficListVo.getData().size());


                    rcaTicketAl = rcaTicketAlFactory.getObject();
                    rcaTicketAlList = new ArrayList<>();

                    rcaTicketAl.setRootCauseNoxiousTrfficAlarmInfoA(noxiousTrffic);
                    rcaTicketAl.setTicketId(ticketId);
                    rcaTicketAl.setRootCauseSysnameA(noxiousTrffic.getStrs_ip());
                    rcaTicketAl.setRootCausePortA(String.valueOf(noxiousTrffic.getStrs_port()));

                    rcaTicketAl.setRootCauseSysnameZ(noxiousTrffic.getStrd_ip());
                    rcaTicketAl.setRootCausePortZ(String.valueOf(noxiousTrffic.getStrd_port()));

                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("ticketId", ticketId);
                    parameterMap.put("strresip", noxiousTrffic.getStrresip());
                    parameterMap.put("strsIp", noxiousTrffic.getStrs_ip());
                    parameterMap.put("strsPort", noxiousTrffic.getStrs_port() + "");
                    parameterMap.put("strdIp", noxiousTrffic.getStrd_ip());
                    parameterMap.put("strdPort", noxiousTrffic.getStrd_port() + "");
                    parameterMap.put("dateregdate", noxiousTrffic.getDateregdate());
                    ticketService.updateNoxiousTrafficTicketId(parameterMap);

                    rcaTicketAlList.add(rcaTicketAl);


                    if(rcaTicketAlList != null) {
                        rcaTicket.setTicketAlList(rcaTicketAlList);
                    }

                    rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);

                    if(rcaTicketResult != null && rcaTicketResult.isResult()){
                        rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                        rcaTicket.setStatus(rcaTicketResult.getValue());

                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);
                        rcaEngineResult.setTicketType("RT");

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                        parameterMap.put("ticketUpdateTime", rcaTicket.getTicketGenerationTime()+"");
                        ticketService.updateRcaTicketUpdateTime(parameterMap);
                    } else {
                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                        rcaEngineResult.setTicketType("RT");
                    }

                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                    LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                    ticketService.insertRcaTicket(rcaTicket);
                    LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                    ticketService.insertRcaTicketAl(rcaTicketAlList);

                    rcaTicketStatus = rcaHandlingStatusObjectFactory.getObject();
                    rcaTicketStatus.setTicketId(rcaTicket.getTicketId());
                    rcaTicketStatus.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                    rcaTicketStatus = rcaHandlingStatusObjectFactory.getObject();
                    rcaTicketStatus.setTicketId(rcaTicket.getTicketId());
                    rcaTicketStatus.setHandlingUser("AI");
                    rcaTicketStatus.setAutoMation("Y");
                    rcaTicketStatus.setHandlingTime(UtlDateHelper.getCurrentTime());
                    rcaTicketStatus.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                    rcaTicketStatus.setTicketType("NTT");
                    rcaTicketStatus.setTicketGenerationTime(rcaTicket.getTicketGenerationTime());

                    ticketService.insertRCATicketHandlingStatus(rcaTicketStatus);
                    ticketService.insertRCATicketHandlingStatusHist(rcaTicketStatus);

                    if(StringUtils.isNotEmpty(rcaTicket.getParentTicketId())){
                        ticketService.updateTicketCnt(rcaTicket);

                        updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());

                        properties = new HashMap<String, String>();
                        properties.put("child_count", updateCnt + "");
                        properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime()+"");

                        rcaEngineResult.setProperties(properties);
                    }

                    ticketService.insertRcaTicketCnt(rcaTicket);

                    rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);
//= = = = = = = = = = = = = = = = = = = = = = 자가최적화(NTT) 큐 = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
                    engineToAiNoxVo = autoProcessMapper.selectEngineToAiNox(rcaTicket.getTicketId());

                    niaEngineToAiNoxAmqp.sendMessageCmd(engineToAiNoxVo);

//= = = = = = = = = = = = = = = = = = = = = = 자가최적화(NTT) 현황 테이블 저장 = = = = = = = = = = = = = = = = = = = = = = = = = =
//                        String autoTreatProcessMaxNoVo = autoProcessMapper.selectAutoProcessMaxNo();
//                        int self_process_no = Integer.parseInt(autoTreatProcessMaxNoVo);

                    autoProcessInserMap = new HashMap<>();
//                        autoProcessInserMap.put("selfProcessNo", String.valueOf(self_process_no+1));
                    if (rcaTicket.getParentTicketId() == null){
                        autoProcessInserMap.put("selfProcessGroup","SO");
                        autoProcessInserMap.put("selfProcessType","N");
                        autoProcessInserMap.put("occur_time", String.valueOf(rcaTicket.getTicketGenerationTime()));
                        autoProcessInserMap.put("ticketId",rcaTicket.getTicketId());
                        autoProcessInserMap.put("ticketType",rcaTicket.getTicketType());
                        autoProcessInserMap.put("alarmno",null);

                        autoProcessMapper.insertAutoProcess(autoProcessInserMap);

                        LOGGER.info("self_process insert ======> "+ rcaTicket.getTicketId());

                    }
//                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createNoxiousTrfficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createNodeFactorTicket(NodeFactorListVo nodeFactorListVo) {
        LOGGER.info("==========>[RcaTrafficTicketService] createNodeFactorTicket : " + nodeFactorListVo + " <==============");
        Map<String, String> properties;
        int updateCnt = 0;

        String ticketId;
        Timestamp faultTime;
        RCATicketAl rcaTicketAl;
        RcaEngineResult rcaEngineResult;

        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;

        NodeFactorInfoVo nodeFactorVoInfo;

        try {
            if(nodeFactorListVo != null && nodeFactorListVo.getData().size() > 0) {
                for(NodeFactorVo nodeFactorVo : nodeFactorListVo.getData()) {
                    faultTime  = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(nodeFactorVo.getMeasured_datetime())));

//                    String time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
//                    String time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
//                    Timestamp time3 = UtlDateHelper.stringToTimestamp(time1);
//                    Timestamp time4 = UtlDateHelper.stringToTimestamp(time2);

//                    if(faultTime.getTime() > time4.getTime()) {
                    rcaTicket = rcaTicketFactory.getObject();
                    ticketId = ticketService.selectTicketKey();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_NODE_FACTOR_TICKET);
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setFaultTime(faultTime);
                    rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_FACTOR_FAIL);
                    rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_FACTOR);
                    rcaTicket.setTicketRcaResultCode("EQUIPMENT_OVERLOAD_DETECTION");
                    rcaTicket.setTicketRcaResultDtlCode("장비 과부하 탐지");
                    rcaTicket.setOccur(true);
                    rcaTicket.setRelatedAlarmCnt(nodeFactorListVo.getData().size());

                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("node_id", nodeFactorVo.getId());
                    parameterMap.put("measured_datetime", String.valueOf(nodeFactorVo.getMeasured_datetime()));


                    nodeFactorVoInfo = nodeFactorMapper.selectNodeFactor(parameterMap);


                    if (nodeFactorVoInfo != null){
                        rcaTicketAl = rcaTicketAlFactory.getObject();
                        rcaTicketAlList = new ArrayList<>();

                        rcaTicketAl.setTicketId(ticketId);
                        rcaTicketAl.setRootCauseSysnameA(nodeFactorVoInfo.getNodeId());


                        parameterMap.put("ticketId",ticketId);
                        nodeFactorMapper.updateNodeFactor(parameterMap);

                        rcaTicketAlList.add(rcaTicketAl);


                        if(rcaTicketAlList != null) {
                            rcaTicket.setTicketAlList(rcaTicketAlList);
                        }

                        RcaTicketResult rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);

                        if(rcaTicketResult != null && rcaTicketResult.isResult()){

                            rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                            rcaTicket.setStatus(rcaTicketResult.getValue());

                            rcaEngineResult = new RcaEngineResult();
                            rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                            rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);
                            rcaEngineResult.setTicketType("RT");

                            parameterMap = new HashMap<String, String>();
                            parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                            parameterMap.put("ticketUpdateTime", rcaTicket.getTicketGenerationTime()+"");
                            ticketService.updateRcaTicketUpdateTime(parameterMap);
                        } else {
                            rcaEngineResult = new RcaEngineResult();
                            rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                            rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                            rcaEngineResult.setTicketType("RT");
                        }

                        rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                        LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                        ticketService.insertRcaTicket(rcaTicket);
                        LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                        ticketService.insertRcaTicketAl(rcaTicketAlList);

                        if(StringUtils.isNotEmpty(rcaTicket.getParentTicketId())){
                            ticketService.updateTicketCnt(rcaTicket);

                            updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());

                            properties = new HashMap<String, String>();
                            properties.put("child_count", updateCnt + "");
                            properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime()+"");

                            rcaEngineResult.setProperties(properties);
                        }

                        ticketService.insertRcaTicketCnt(rcaTicket);

                        rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);
                    }
//                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createNodeFactorTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createSdnTrafficTicket(SdnTrafficListVo sdnTrafficListVo) {
        LOGGER.info("==========>[RcaTrafficTicketService] createAnomalousTrafficTicket : " + sdnTrafficListVo + " <==============");

        try{
            if(sdnTrafficListVo != null && sdnTrafficListVo.getData().size() > 0) {
                Iterator<SdnTrafficVo> itr = sdnTrafficListVo.getData().iterator();
                while (itr.hasNext()) {
                    // sdnTrafficInfoVo 조회
                    SdnTrafficVo sdnTrafficVo = itr.next();
                    SdnTrafficInfoVo sdnTrafficInfoVo= null;
                    HashMap<String, String> parameterMap = null;
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("strifid", sdnTrafficVo.getStrifid());
                    parameterMap.put("measured_datetime", sdnTrafficVo.getMeasured_datetime() + "");
                    sdnTrafficInfoVo = sdnTrafficMapper.selectATT2Alarm(parameterMap);

                    LOGGER.info("==========>[RcaTrafficTicketService] sdnTrafficInfoVo : " + sdnTrafficInfoVo + " <==============");

                    // rcaTicket 변수 셋팅
                    rcaTicket = rcaTicketFactory.getObject();
                    String ticketId = ticketService.selectTicketKey();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_ANOMALOUS2_TRAFFIC_TICKET);
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setFaultTime(sdnTrafficInfoVo.getMeasuredDatetime());
                    rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_TRAFFIC_FAIL);
                    rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_TRAFFIC);
                    rcaTicket.setOccur(true);
                    rcaTicket.setTicketRcaResultCode(sdnTrafficInfoVo.getTicketRcaResultCode());
                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                    rcaTicket.setRelatedAlarmCnt(1);

                    // TB_ANOMALOUS_1MI_TRAFFIC TICKET_ID UPDATE (JOIN 해서 사용할 수 있도록)
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("ticketId", rcaTicket.getTicketId());
                    parameterMap.put("strifid", sdnTrafficInfoVo.getStrifid());
                    parameterMap.put("strresid", sdnTrafficInfoVo.getStrresid());
                    parameterMap.put("inttimestamp", sdnTrafficInfoVo.getMeasuredDatetime().getTime() + "");
                    sdnTrafficMapper.updateSdnTraffic(parameterMap);

                    // rcaTicketAl 변수 셋팅
                    RCATicketAl rcaTicketAl = rcaTicketAlFactory.getObject();
                    rcaTicketAl.setTicketId(ticketId);
                    rcaTicketAl.setRootCauseSysnameA(sdnTrafficInfoVo.getNodeid());
                    rcaTicketAl.setRootCausePortA(sdnTrafficInfoVo.getStrifnm());

                    // RcaResultDtlCode 및 sysnameZ, portZ 셋팅
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("resid", sdnTrafficInfoVo.getStrresid());
                    parameterMap.put("ifid", sdnTrafficInfoVo.getStrifid());
                    BackboneLinkVo backboneLinkVo = topologyMapper.selectBackboneTopology(parameterMap);
                    if (backboneLinkVo != null) {
                        rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(백본)");
                        if (sdnTrafficInfoVo.getStrresid().equals(backboneLinkVo.getSrcNodeNum())) {
                            rcaTicketAl.setRootCauseSysnameZ(backboneLinkVo.getDestNodeId());
                            rcaTicketAl.setRootCausePortZ(backboneLinkVo.getDestIfId());
                        } else {
                            rcaTicketAl.setRootCauseSysnameZ(backboneLinkVo.getSrcNodeId());
                            rcaTicketAl.setRootCausePortZ(backboneLinkVo.getSrcIfId());
                        }
                    } else {
                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("nodeId", sdnTrafficInfoVo.getStrresnm());
                        parameterMap.put("ifId", sdnTrafficInfoVo.getStrifnm());
                        UserOrganVo userOrganVo = ticketService.selectUserOrgan(parameterMap);
                        if (userOrganVo != null) {
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(이용기관)");
                            rcaTicketAl.setRootCauseSysnameZ(userOrganVo.getNrenName());
                            rcaTicketAl.setRootCausePortZ(userOrganVo.getIfId());
                        } else {
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(포트)");
                            rcaTicketAl.setRootCauseSysnameZ("Unknown");
                        }
                    }

                    // rcaTrafficeTicketMerge에서 사용하므로 ticketAl 셋팅해야함.
                    List<RCATicketAl> rcaTicketAlList = new ArrayList<>();
                    rcaTicketAlList.add(rcaTicketAl);
                    rcaTicket.setTicketAlList(rcaTicketAlList);

                    // 티켓 중복 체크 -> UI에 전달할 신호 생성
                    RcaTicketResult rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);
                    RcaEngineResult rcaEngineResult = new RcaEngineResult();
                    if (rcaTicketResult != null && rcaTicketResult.isResult()) {
                        rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                        rcaTicket.setStatus(rcaTicketResult.getValue());

                        rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);
                        rcaEngineResult.setTicketType("RT");

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                        parameterMap.put("ticketUpdateTime", rcaTicket.getTicketGenerationTime() + "");
                        ticketService.updateRcaTicketUpdateTime(parameterMap);
                    } else {
                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                        rcaEngineResult.setTicketType("RT");
                    }

                    LOGGER.info("==========>[RcaTicketManager] createAnomalousTrafficTicket createTicket : " + rcaTicket.toString() + "<==============");
                    LOGGER.info("==========>[RcaTicketManager] createAnomalousTrafficTicket rcaTicketAlList : " + rcaTicketAlList + "<==============");
                    ticketService.insertRcaTicket(rcaTicket);  // RCA Ticket 발행
                    ticketService.insertRcaTicketAl(rcaTicketAlList); // RCA Ticket Al 발행
                    ticketService.insertRcaTicketCnt(rcaTicket); // cnt 업데이트

                    // UI에 신호 전송(현재 의미없음)
                    if (StringUtils.isNotEmpty(rcaTicket.getParentTicketId())) {
                        ticketService.updateTicketCnt(rcaTicket);
                        int updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());
                        Map<String, String> properties = new HashMap<String, String>();
                        properties.put("child_count", updateCnt + "");
                        properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime() + "");
                        rcaEngineResult.setProperties(properties);
                    }
                    rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);

                    // 큐로 AI측으로 이상트래픽 데이터 전송
                    EngineToAiAnoVo engineToAiAnoVo = autoProcessMapper.selectEngineToAiAno(rcaTicket.getTicketId());
                    engineToAiAnoVo.setNodeId(sdnTrafficInfoVo.getNodeid());
                    engineToAiAnoVo.setIfId(sdnTrafficInfoVo.getStrifnm());
                    niaEngineToAiAnoAmqp.sendMessageCmd(engineToAiAnoVo);

                    // 자가최적화(ATT2) 현황 테이블 저장
                    if (rcaTicket.getParentTicketId() == null){
                        HashMap<String, String> autoProcessInserMap = new HashMap<>();
                        autoProcessInserMap.put("selfProcessGroup","SO");
                        autoProcessInserMap.put("selfProcessType","A");
                        autoProcessInserMap.put("occur_time", String.valueOf(rcaTicket.getTicketGenerationTime()));
                        autoProcessInserMap.put("ticketId",rcaTicket.getTicketId());
                        autoProcessInserMap.put("ticketType",rcaTicket.getTicketType());
                        autoProcessInserMap.put("alarmno",null);
                        autoProcessMapper.insertAutoProcess(autoProcessInserMap);
                        LOGGER.info("self_process insert ======> "+ rcaTicket.getTicketId());
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createSdnTrafficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }


    @Override
    public void createAttAiTicket(SdnTrafficListVo sdnTrafficListVo, String gb) {
        LOGGER.info("==========>[RcaTrafficTicketService] createAttAiTicket : " + sdnTrafficListVo + " <==============");

        try{
            if(sdnTrafficListVo != null && sdnTrafficListVo.getData().size() > 0) {
                Iterator<SdnTrafficVo> itr = sdnTrafficListVo.getData().iterator();
                while (itr.hasNext()) {
                    SdnTrafficVo sdnTrafficVo = itr.next();
                    HashMap<String, String> parameterMap = null;
                    SdnTrafficInfoVo sdnTrafficInfoVo = new SdnTrafficInfoVo();
                    sdnTrafficInfoVo.setStrresid(sdnTrafficVo.getStrresid());
                    sdnTrafficInfoVo.setStrresnm(sdnTrafficVo.getStrresnm());
                    sdnTrafficInfoVo.setNodeid(sdnTrafficVo.getNode_id());
                    sdnTrafficInfoVo.setStrifid(sdnTrafficVo.getStrifid());
                    sdnTrafficInfoVo.setStrifnm(sdnTrafficVo.getStrifnm());
                    sdnTrafficInfoVo.setTicketRcaResultCode(sdnTrafficVo.getTicketRcaResultCode());

                    Instant instant = Instant.ofEpochMilli(sdnTrafficVo.getMeasured_datetime());
                    ZonedDateTime kst = instant.atZone(ZoneId.of("Asia/Seoul"));
                    sdnTrafficInfoVo.setMeasuredDatetime(Timestamp.from(kst.toInstant()));

                    LOGGER.info("==========>[RcaTrafficTicketService] createAttAiTicket : " + sdnTrafficInfoVo + " <==============");

                    // rcaTicket 변수 셋팅
                    rcaTicket = rcaTicketFactory.getObject();
                    String ticketId = ticketService.selectTicketKey();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_ANOMALOUS2_AIB_TRAFFIC_TICKET);
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setFaultTime(sdnTrafficInfoVo.getMeasuredDatetime());
                    rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_TRAFFIC_FAIL);
                    rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_TRAFFIC);
                    rcaTicket.setOccur(true);

                    if(sdnTrafficInfoVo.getTicketRcaResultCode() != null && sdnTrafficInfoVo.getTicketRcaResultCode().equals("이상트래픽 TCA 경보")){
                        rcaTicket.setTicketRcaResultCode(sdnTrafficInfoVo.getTicketRcaResultCode());
                    }else{
                        rcaTicket.setTicketRcaResultCode("이상트래픽 예측 경보");
                    }
                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                    rcaTicket.setRelatedAlarmCnt(1);

                    // rcaTicketAl 변수 셋팅
                    RCATicketAl rcaTicketAl = rcaTicketAlFactory.getObject();
                    rcaTicketAl.setTicketId(ticketId);
                    rcaTicketAl.setRootCauseSysnameA(sdnTrafficInfoVo.getNodeid());
                    rcaTicketAl.setRootCausePortA(sdnTrafficInfoVo.getStrifnm());

                    // RcaResultDtlCode 및 sysnameZ, portZ 셋팅
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("resid", sdnTrafficInfoVo.getStrresid());
                    parameterMap.put("ifid", sdnTrafficInfoVo.getStrifid());
                    BackboneLinkVo backboneLinkVo = topologyMapper.selectBackboneTopology(parameterMap);
                    if (backboneLinkVo != null) {
                        rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(백본)");
                        if (sdnTrafficInfoVo.getStrresid().equals(backboneLinkVo.getSrcNodeNum())) {
                            rcaTicketAl.setRootCauseSysnameZ(backboneLinkVo.getDestNodeId());
                            rcaTicketAl.setRootCausePortZ(backboneLinkVo.getDestIfId());
                        } else {
                            rcaTicketAl.setRootCauseSysnameZ(backboneLinkVo.getSrcNodeId());
                            rcaTicketAl.setRootCausePortZ(backboneLinkVo.getSrcIfId());
                        }
                    } else {
                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("nodeId", sdnTrafficInfoVo.getStrresnm());
                        parameterMap.put("ifId", sdnTrafficInfoVo.getStrifnm());
                        UserOrganVo userOrganVo = ticketService.selectUserOrgan(parameterMap);
                        if (userOrganVo != null) {
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(이용기관)");
                            rcaTicketAl.setRootCauseSysnameZ(userOrganVo.getNrenName());
                            rcaTicketAl.setRootCausePortZ(userOrganVo.getIfId());
                        } else {
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(포트)");
                            rcaTicketAl.setRootCauseSysnameZ("Unknown");
                        }
                    }

                    // rcaTrafficeTicketMerge에서 사용하므로 ticketAl 셋팅해야함.
                    List<RCATicketAl> rcaTicketAlList = new ArrayList<>();
                    rcaTicketAlList.add(rcaTicketAl);
                    rcaTicket.setTicketAlList(rcaTicketAlList);

                    // 티켓 중복 체크 -> UI에 전달할 신호 생성
                    RcaTicketResult rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);
                    RcaEngineResult rcaEngineResult = new RcaEngineResult();
                    if (rcaTicketResult != null && rcaTicketResult.isResult()) {
                        rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                        rcaTicket.setStatus(rcaTicketResult.getValue());

                        rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);
                        rcaEngineResult.setTicketType("RT");

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                        parameterMap.put("ticketUpdateTime", rcaTicket.getTicketGenerationTime() + "");
                        ticketService.updateRcaTicketUpdateTime(parameterMap);
                    } else {
                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                        rcaEngineResult.setTicketType("RT");
                    }

                    LOGGER.info("==========>[RcaTicketManager] createAttAiTicket createTicket : " + rcaTicket.toString() + "<==============");
                    LOGGER.info("==========>[RcaTicketManager] createAttAiTicket rcaTicketAlList : " + rcaTicketAlList + "<==============");
                    ticketService.insertRcaTicket(rcaTicket);  // RCA Ticket 발행
                    ticketService.insertRcaTicketAl(rcaTicketAlList); // RCA Ticket Al 발행
                    ticketService.insertRcaTicketCnt(rcaTicket); // cnt 업데이트

                    // UI에 신호 전송(현재 의미없음)
                    if (StringUtils.isNotEmpty(rcaTicket.getParentTicketId())) {
                        ticketService.updateTicketCnt(rcaTicket);
                        int updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());
                        Map<String, String> properties = new HashMap<String, String>();
                        properties.put("child_count", updateCnt + "");
                        properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime() + "");
                        rcaEngineResult.setProperties(properties);
                    }
                    rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);

                    // 자가최적화(ATT2) 현황 테이블 저장
                    if (rcaTicket.getParentTicketId() == null){
                        HashMap<String, String> autoProcessInserMap = new HashMap<>();
                        autoProcessInserMap.put("selfProcessGroup","SO");
                        autoProcessInserMap.put("selfProcessType","A");
                        autoProcessInserMap.put("occur_time", String.valueOf(rcaTicket.getTicketGenerationTime()));
                        autoProcessInserMap.put("ticketId",rcaTicket.getTicketId());
                        autoProcessInserMap.put("ticketType",rcaTicket.getTicketType());
                        autoProcessInserMap.put("alarmno",null);
                        autoProcessMapper.insertAutoProcess(autoProcessInserMap);
                        LOGGER.info("self_process insert ======> "+ rcaTicket.getTicketId());
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createAttAiTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createSyslogTicket(SyslogListVo syslogListVo) {
        LOGGER.info("==========>[RcaTrafficTicketService] createSyslogTicket : " + syslogListVo + " <==============");
        Map<String, String> properties;
        int updateCnt = 0;


        String ticketId;
        Timestamp faultTime;
        RCATicketAl rcaTicketAl;
        RcaEngineResult rcaEngineResult;
        RcaTicketResult rcaTicketResult = null;
        RCAHandlingStatus rcaTicketStatus;

        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;

        NodeFactorInfoVo syslogInfoVo;

        try {
            if(syslogListVo != null && syslogListVo.getData().size() > 0) {
                for(SyslogVo syslogVo : syslogListVo.getData()) {
                    faultTime  = Timestamp.valueOf(syslogVo.getCollect_timestamp());

//                    String time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
//                    String time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
//                    Timestamp time3 = UtlDateHelper.stringToTimestamp(time1);
//                    Timestamp time4 = UtlDateHelper.stringToTimestamp(time2);

//                    if(faultTime.getTime() > time4.getTime()) {
                    rcaTicket = rcaTicketFactory.getObject();
                    ticketId = ticketService.selectTicketKey();
                    rcaTicket.setTicketId(ticketId);
                    rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_SYSLOG_TICKET);
                    rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                    rcaTicket.setFaultTime(faultTime);
                    rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_TRAFFIC_FAIL);
                    rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_TRAFFIC);
                    rcaTicket.setTicketRcaResultCode("SYSLOG_DETECTION");
                    rcaTicket.setTicketRcaResultDtlCode("SYSLOG 탐지");
                    rcaTicket.setOccur(true);
                    rcaTicket.setRelatedAlarmCnt(syslogListVo.getData().size());


                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("collect_seq", syslogVo.getCollect_seq());
                    parameterMap.put("collect_timestamp", String.valueOf(syslogVo.getCollect_timestamp()));

//                        syslog mapper 생성 후 select 하는 쿼리 .
                    syslogInfoVo = nodeFactorMapper.selectNodeFactor(parameterMap);


                    if(syslogInfoVo != null) {
                        rcaTicketAl = rcaTicketAlFactory.getObject();
                        rcaTicketAlList = new ArrayList<>();

                        rcaTicketAl.setTicketId(ticketId);
                        rcaTicketAl.setRootCauseSysnameA(syslogInfoVo.getNodeId());

//                          syslog로 변경해주기.
                        parameterMap.put("ticketId",ticketId);
                        nodeFactorMapper.updateNodeFactor(parameterMap);

                        rcaTicketAlList.add(rcaTicketAl);
                    }

                    if(rcaTicketAlList != null) {
                        rcaTicket.setTicketAlList(rcaTicketAlList);
                    }
                    rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);

                    if(rcaTicketResult != null && rcaTicketResult.isResult()){

                        rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                        rcaTicket.setStatus(rcaTicketResult.getValue());

                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getParentTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_MERGE);
                        rcaEngineResult.setTicketType("RT");

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", rcaTicket.getParentTicketId());
                        parameterMap.put("ticketUpdateTime", rcaTicket.getTicketGenerationTime()+"");
                        ticketService.updateRcaTicketUpdateTime(parameterMap);
                    } else {
                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                        rcaEngineResult.setTicketType("RT");
                    }

                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                    LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                    ticketService.insertRcaTicket(rcaTicket);
                    LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                    ticketService.insertRcaTicketAl(rcaTicketAlList);

                    if(StringUtils.isNotEmpty(rcaTicket.getParentTicketId())){
                        ticketService.updateTicketCnt(rcaTicket);

                        updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());

                        properties = new HashMap<String, String>();
                        properties.put("child_count", updateCnt + "");
                        properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime()+"");

                        rcaEngineResult.setProperties(properties);
                    }

                    ticketService.insertRcaTicketCnt(rcaTicket);

                    rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);
//                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createSyslogTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }

    }
}
