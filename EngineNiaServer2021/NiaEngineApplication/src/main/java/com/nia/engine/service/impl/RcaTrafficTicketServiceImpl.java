package com.nia.engine.service.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.service.*;
import com.nia.engine.vo.*;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrfficVo;
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
    @Qualifier("RcaTicketManagerService")
    private RcaTicketManagerService rcaTicketManagerService;

    @Autowired
    private RCATicket rcaTicket;

    @Value("${spring.profiles}")
    private String profiles;

    @Override
    public void createAnomalousTrafficTicket(AnomalousTrafficListVo anomalousTrafficListVo){
        LOGGER.info("==========>[RcaTrafficTicketService] createAnomalousTrafficTicket : " + anomalousTrafficListVo + " <==============");
        String time1 = null;
        String time2 = null;
        Timestamp time3 = null;
        Timestamp time4 = null;

        String ticketId;
        Timestamp faultTime;
        AnomalousTrafficVo anomalousTrafficVo;
        AnomalousTrafficVo tmpAnomalousTrafficVo;
        RcaEngineResult rcaEngineResult;
        RCATicketAl rcaTicketAl;
        TopologyObject topology;

        Iterator<AnomalousTrafficVo> itr;
        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;

        try{
            if(anomalousTrafficListVo != null && anomalousTrafficListVo.getData().size() > 0){
                itr = anomalousTrafficListVo.getData().iterator();

                while( itr.hasNext() ) {
                    anomalousTrafficVo = itr.next();

                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("strifid", anomalousTrafficVo.getStrifid());
                    parameterMap.put("inttimestamp", anomalousTrafficVo.getInttimestamp() + "");
                    tmpAnomalousTrafficVo = ticketService.selectAnomalousTrafficAlarm(parameterMap);

                    if(tmpAnomalousTrafficVo != null) {
                        anomalousTrafficVo.setIfId(tmpAnomalousTrafficVo.getIfId());
                        anomalousTrafficVo.setStrreid(tmpAnomalousTrafficVo.getStrreid());
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
                        rcaTicket.setTicketRcaResultCode("TRAFFIC_ANOMALOUS_DETECTION");
                        rcaTicket.setTicketRcaResultDtlCode("이상 트래픽 탐지");
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
                        parameterMap.put("sysname", anomalousTrafficVo.getNodeId());
                        parameterMap.put("port", anomalousTrafficVo.getIfId());
                        topology = topologyService.selectE2eTopology(parameterMap);

                        if(topology != null){
                            rcaTicketAl.setRootCauseSysnameZ(topology.getOppSysname());
                            rcaTicketAl.setRootCausePortZ(topology.getOppPort());
                        }

                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("ticketId", ticketId);
                        parameterMap.put("strifid", anomalousTrafficVo.getStrifid());
                        parameterMap.put("ifId", anomalousTrafficVo.getIfId());
                        parameterMap.put("inttimestamp", anomalousTrafficVo.getInttimestamp() + "");
                        ticketService.updateAnomalousTrafficTicketId(parameterMap);

                        ticketService.insertRcaTicketCnt(rcaTicket);

                        rcaTicketAlList.add(rcaTicketAl);

                        if (rcaTicketAlList != null) {
                            rcaTicket.setTicketAlList(rcaTicketAlList);
                        }
                        rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);


                        LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                        ticketService.insertRcaTicket(rcaTicket);
                        LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                        ticketService.insertRcaTicketAl(rcaTicketAlList);

                        rcaEngineResult = new RcaEngineResult();
                        rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                        rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                        rcaEngineResult.setTicketType("RT");

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

        String ticketId;
        Timestamp faultTime;
        RCATicketAl rcaTicketAl;
        RcaEngineResult rcaEngineResult;

        List<RCATicketAl> rcaTicketAlList = null;
        HashMap<String, String> parameterMap;

        try {
            if (noxiousTrafficListVo != null && noxiousTrafficListVo.getData().size() > 0) {
                faultTime  = Timestamp.valueOf(noxiousTrafficListVo.getData().get(0).getDateregdate());

                time1 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 00:00:00";
                time2 = UtlDateHelper.timestampToString("yyyy-MM-dd", faultTime) + " 06:00:00";
                time3 = UtlDateHelper.stringToTimestamp(time1);
                time4 = UtlDateHelper.stringToTimestamp(time2);

                if (faultTime.getTime() > time4.getTime()) {
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

                    for(NoxiousTrfficVo noxiousTrffic :noxiousTrafficListVo.getData()){
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
                    }

                    if (rcaTicketAlList != null) {
                        rcaTicket.setTicketAlList(rcaTicketAlList);
                    }
                    rcaTicket.setStatus(RcaCodeInfo.TICKET_STATUS_INIT);

                    LOGGER.info("==========>[RcaTicketManager] createTicket : " + rcaTicket.toString() + "<==============");
                    ticketService.insertRcaTicket(rcaTicket);
                    LOGGER.info("==========>[RcaTicketManager] rcaTicketAlList : " + rcaTicketAlList + "<==============");
                    ticketService.insertRcaTicketAl(rcaTicketAlList);

                    ticketService.insertRcaTicketCnt(rcaTicket);

                    rcaEngineResult = new RcaEngineResult();
                    rcaEngineResult.setTicketId(rcaTicket.getTicketId());
                    rcaEngineResult.setEventType(RcaCodeInfo.UI_TICKET_TYPE_NEW);
                    rcaEngineResult.setTicketType("RT");

                    rcaTicketManagerService.uiSendTicketResult(rcaEngineResult);
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTrafficTicketService] createNoxiousTrfficTicket error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }
}
