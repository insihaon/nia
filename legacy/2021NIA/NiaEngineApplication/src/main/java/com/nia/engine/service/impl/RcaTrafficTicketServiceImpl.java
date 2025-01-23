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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Autowired
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
        String time1 = null;
        String time2 = null;
        Timestamp time3 = null;
        Timestamp time4 = null;
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
        RcaTicketResult rcaTicketResult = null;
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

                    time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
                    time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
                    time3 = UtlDateHelper.stringToTimestamp(time1);
                    time4 = UtlDateHelper.stringToTimestamp(time2);

                    if (faultTime.getTime() > time4.getTime()) {
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
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createAnomalousTrafficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createNoxiousTrfficTicket(NoxiousTrafficListVo noxiousTrafficListVo){
        LOGGER.info("==========>[RcaTrafficTicketService] createNoxiousTrfficTicket : " + noxiousTrafficListVo + " <==============");
        String time1 = null;
        String time2 = null;
        Timestamp time3 = null;
        Timestamp time4 = null;
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

                    time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
                    time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
                    time3 = UtlDateHelper.stringToTimestamp(time1);
                    time4 = UtlDateHelper.stringToTimestamp(time2);

                    if(faultTime.getTime() > time4.getTime()) {
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


                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createNoxiousTrfficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createNodeFactorTicket(NodeFactorListVo nodeFactorListVo) {
        LOGGER.info("==========>[RcaTrafficTicketService] createNodeFactorTicket : " + nodeFactorListVo + " <==============");
        String time1 = null;
        String time2 = null;
        Timestamp time3 = null;
        Timestamp time4 = null;
        Map<String, String> properties;
        int updateCnt = 0;

        String ticketId;
        Timestamp faultTime;
        RCATicketAl rcaTicketAl;
        RcaEngineResult rcaEngineResult;
        RcaTicketResult rcaTicketResult = null;

        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;

        NodeFactorInfoVo nodeFactorVoInfo;

        try {
            if(nodeFactorListVo != null && nodeFactorListVo.getData().size() > 0) {
                for(NodeFactorVo nodeFactorVo : nodeFactorListVo.getData()) {
                    faultTime  = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(nodeFactorVo.getMeasured_datetime())));

                    time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
                    time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
                    time3 = UtlDateHelper.stringToTimestamp(time1);
                    time4 = UtlDateHelper.stringToTimestamp(time2);

                    if(faultTime.getTime() > time4.getTime()) {
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

                        }
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createNodeFactorTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createSdnTrafficTicket(SdnTrafficListVo sdnTrafficListVo) {
        LOGGER.info("==========>[RcaTrafficTicketService] createAnomalousTrafficTicket : " + sdnTrafficListVo + " <==============");
        String time1 = null;
        String time2 = null;
        Timestamp time3 = null;
        Timestamp time4 = null;
        Map<String, String> properties;
        int updateCnt = 0;

        String ticketId;
        Timestamp faultTime;
        SdnTrafficVo sdnTrafficVo;
        SdnTrafficVo tmpSdnTrafficVo;
        RcaEngineResult rcaEngineResult;
        RCATicketAl rcaTicketAl;
        UserOrganVo userOrganVo;
        BackboneLinkVo backboneLinkVo;
        RcaTicketResult rcaTicketResult = null;
        EngineToAiAnoVo engineToAiAnoVo;

        Iterator<SdnTrafficVo> itr;
        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;

        SdnTrafficInfoVo sdnTrafficInfoVo;
        HashMap<String, String> autoProcessInserMap;


        try{
            if(sdnTrafficListVo != null && sdnTrafficListVo.getData().size() > 0) {
                itr = sdnTrafficListVo.getData().iterator();

                while (itr.hasNext()) {
                    sdnTrafficVo = itr.next();
                    String stringTimestamp = String.valueOf(sdnTrafficVo.getMeasured_datetime());
                    long unixTimestamp = Long.parseLong(stringTimestamp);
                    Timestamp timestamp = new Timestamp(unixTimestamp * 1000L);


                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("strifid", sdnTrafficVo.getStrifid());
                    parameterMap.put("measured_datetime", sdnTrafficVo.getMeasured_datetime() + "");

                    sdnTrafficInfoVo = sdnTrafficMapper.selectATT2Alarm(parameterMap);

                    if (sdnTrafficInfoVo != null) {
                        sdnTrafficVo.setStrifid(sdnTrafficInfoVo.getStrifid());
                        sdnTrafficVo.setStrresid(sdnTrafficInfoVo.getStrresid());
                        sdnTrafficVo.setNode_id(sdnTrafficInfoVo.getNodeid());
                    }

                    faultTime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(sdnTrafficVo.getMeasured_datetime())));

                    time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
                    time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
                    time3 = UtlDateHelper.stringToTimestamp(time1);
                    time4 = UtlDateHelper.stringToTimestamp(time2);

                    if (faultTime.getTime() > time4.getTime()) {
                        rcaTicket = rcaTicketFactory.getObject();
                        ticketId = ticketService.selectTicketKey();
                        rcaTicket.setTicketId(ticketId);
                        rcaTicket.setTicketType(RcaCodeInfo.TICKET_TYPE_ANOMALOUS2_TRAFFIC_TICKET);
                        rcaTicket.setTicketGenerationTime(UtlDateHelper.getCurrentTime());
                        rcaTicket.setFaultTime(faultTime);
                        rcaTicket.setRootCauseType(RcaCodeInfo.RCA_RESULT_TRAFFIC_FAIL);
                        rcaTicket.setRootCauseDomain(RcaCodeInfo.DOMAIN_TRAFFIC);
                        rcaTicket.setOccur(true);

                        rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);
                        rcaTicket.setRelatedAlarmCnt(1);

                        rcaTicketAl = rcaTicketAlFactory.getObject();
                        rcaTicketAlList = new ArrayList<>();


                        rcaTicketAl.setTicketId(ticketId);
                        rcaTicketAl.setRootCauseSysnameA(sdnTrafficInfoVo.getNodeid());
                        rcaTicketAl.setRootCausePortA(sdnTrafficInfoVo.getStrifnm());

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("resid", sdnTrafficInfoVo.getStrresid());
                        parameterMap.put("ifid", sdnTrafficInfoVo.getStrifid());
                        backboneLinkVo = topologyMapper.selectBackboneTopology(parameterMap);


                        if (backboneLinkVo != null) {
                            rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(백본)");

                            if (sdnTrafficVo.getStrresid().equals(backboneLinkVo.getSrcNodeNum())) {
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
                            userOrganVo = ticketService.selectUserOrgan(parameterMap);


                            if (userOrganVo != null) {
                                rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(이용기관)");

                                rcaTicketAl.setRootCauseSysnameZ(userOrganVo.getNrenName());
                                rcaTicketAl.setRootCausePortZ(userOrganVo.getIfId());
                            } else {
                                rcaTicket.setTicketRcaResultDtlCode("이상 트래픽(포트)");
                                rcaTicketAl.setRootCauseSysnameZ("Unknown");
                            }
                        }

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", rcaTicket.getTicketId());
                        parameterMap.put("strifid", sdnTrafficVo.getStrifid());
                        parameterMap.put("strresid", sdnTrafficVo.getStrresid());
                        parameterMap.put("inttimestamp", sdnTrafficVo.getMeasured_datetime() + "");

                        sdnTrafficMapper.updateSdnTraffic(parameterMap);

                        rcaTicketAlList.add(rcaTicketAl);

                        if (rcaTicketAlList != null) {
                            rcaTicket.setTicketAlList(rcaTicketAlList);
                        }
                        rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                        rcaTicketResult = rcaTicketMergeService.rcaTrafficeTicketMerge(rcaTicket);

                        if (rcaTicketResult != null && rcaTicketResult.isResult()) {

                            rcaTicket.setParentTicketId(rcaTicketResult.getTicketId());
                            rcaTicket.setStatus(rcaTicketResult.getValue());

                            rcaEngineResult = new RcaEngineResult();
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

                        LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                        ticketService.insertRcaTicket(rcaTicket);
                        LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                        ticketService.insertRcaTicketAl(rcaTicketAlList);

                        if (StringUtils.isNotEmpty(rcaTicket.getParentTicketId())) {
                            ticketService.updateTicketCnt(rcaTicket);

                            updateCnt = ticketService.selectChildTicketCnt(rcaTicket.getParentTicketId());

                            properties = new HashMap<String, String>();
                            properties.put("child_count", updateCnt + "");
                            properties.put("ticket_update_time", rcaTicket.getTicketGenerationTime() + "");

                            rcaEngineResult.setProperties(properties);
                        }

                        ticketService.insertRcaTicketCnt(rcaTicket);

                        rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);

//= = = = = = = = = = = = = = = = = = = = = = 자가최적화(ATT2) 큐  = = = = = = = = = = = = = = = = = = = = = = = = = =
                        engineToAiAnoVo = autoProcessMapper.selectEngineToAiAno(rcaTicket.getTicketId());
                        engineToAiAnoVo.setNodeId(sdnTrafficInfoVo.getNodeid());
                        engineToAiAnoVo.setIfId(sdnTrafficInfoVo.getStrifnm());
                        niaEngineToAiAnoAmqp.sendMessageCmd(engineToAiAnoVo);

//= = = = = = = = = = = = = = = = = = = = = = 자가최적화(ATT2) 현황 테이블 저장 = = = = = = = = = = = = = = = = = = = = = = = = = =
//                        String autoTreatProcessMaxNoVo = autoProcessMapper.selectAutoProcessMaxNo();
//                        int self_process_no = Integer.parseInt(autoTreatProcessMaxNoVo);

//                        autoProcessInserMap.put("selfProcessNo", String.valueOf(self_process_no+1));
                        if (rcaTicket.getParentTicketId() == null){

                            autoProcessInserMap = new HashMap<>();
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
            }

        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createSdnTrafficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }


    }

    @Override
    public void createSyslogTicket(SyslogListVo syslogListVo) {
        LOGGER.info("==========>[RcaTrafficTicketService] createSyslogTicket : " + syslogListVo + " <==============");
        String time1 = null;
        String time2 = null;
        Timestamp time3 = null;
        Timestamp time4 = null;
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

                    time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
                    time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
                    time3 = UtlDateHelper.stringToTimestamp(time1);
                    time4 = UtlDateHelper.stringToTimestamp(time2);

                    if(faultTime.getTime() > time4.getTime()) {
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




                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createSyslogTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }

    }
}
