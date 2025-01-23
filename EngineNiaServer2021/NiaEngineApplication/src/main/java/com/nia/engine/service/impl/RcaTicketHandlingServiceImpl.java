package com.nia.engine.service.impl;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.mapper.AutoProcessMapper;
import com.nia.engine.service.RcaTicketHandlingService;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.RCATicket;
import com.nia.engine.vo.RCATicketHandlingStatus;
import com.nia.engine.vo.RcaEngineResult;
import com.nia.engine.vo.aiToEngine.AiToEngineAnoVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private AutoProcessMapper autoProcessMapper;

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
    private RCATicket rcaTicket;

    @Value("${spring.profiles}")
    private String profiles;

    private Map<String, String> properties;

    @Override
    public synchronized void ticketStatusModify (RCATicketHandlingStatus rcaTicketHandlingStatus) {
        RcaEngineResult rcaEngineResult;
        String sopId = "";
        String status = "";
        try {
            LOGGER.info(">>>>>>>>> [RcaTicketHandlingService]   rcaTicketHandlingStatus : " + rcaTicketHandlingStatus.toString() + " <<<<<<<<<<<<<<<<<");

            if ("PF".equals(rcaTicketHandlingStatus.getTicketType())) {
                switch (rcaTicketHandlingStatus.getStatus()) {
                    case "ACK":
                        status = rcaTicketHandlingStatus.getStatus();

                        sopId = ticketService.selectSopKey();
                        rcaTicketHandlingStatus.setSopId(sopId);

                        ticketService.insertSop(rcaTicketHandlingStatus);
                        ticketService.insertSopPerformance(rcaTicketHandlingStatus);
                        break;
                    case "FIN":
                    case "INIT":
                        status = rcaTicketHandlingStatus.getStatus();

                        if (StringUtils.isNotEmpty(rcaTicketHandlingStatus.getSopId())) {
                            sopId = ticketService.selectSopKey();
                            rcaTicketHandlingStatus.setSopId(sopId);
                        }
                        ticketService.upsertSop(rcaTicketHandlingStatus);
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
                    Iterator<RCATicket> itr =
                            ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();

                    while (itr.hasNext()) {
                        rcaTicket = itr.next();

                        if (rcaTicket.getTicketId().equals(rcaTicketHandlingStatus.getTicketId())) {
                            rcaTicket.setStatus(status);
                        }
                    }
                }
            } else if ("NTT".equals(rcaTicketHandlingStatus.getTicketType()) || "ATT".equals(rcaTicketHandlingStatus.getTicketType()) || "ATT2".equals(rcaTicketHandlingStatus.getTicketType()) || "NFTT".equals(rcaTicketHandlingStatus.getTicketType())) {
                status = rcaTicketHandlingStatus.getStatus();

                /* set sopId , AI 추론 결과 */
                sopId = ticketService.selectSopKey();
                rcaTicketHandlingStatus.setSopId(sopId);
                AiToEngineAnoVo aiResult = ticketService.selectAiTicketResult(rcaTicketHandlingStatus.getTicketId());
                rcaTicketHandlingStatus.setZero1Model(aiResult.getZero1Model());
                rcaTicketHandlingStatus.setZero1Entropy(aiResult.getZero1Entropy());

                try {
                    LOGGER.info(">>>>>>>>> [RcaTicketHandlingService]   NTT/ATT2/NFTT UpsertSop");
                    ticketService.upsertSop(rcaTicketHandlingStatus);
                } catch (Exception e) {
                    LOGGER.error("NTT/ATT2/NFTT UpsertSop err >>> " + e);
                }

                if ("INIT".equals(rcaTicketHandlingStatus.getStatus()) || "ACK".equals(rcaTicketHandlingStatus.getStatus())) {
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

                HashMap<String, String> autoProcessUpdateMap;
                autoProcessUpdateMap = new HashMap<>();
                autoProcessUpdateMap.put("ticketId", rcaTicketHandlingStatus.getTicketId());
                autoProcessMapper.updateAutoProcessTicket(autoProcessUpdateMap);

                engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);

                if (((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0) {
                    Iterator<RCATicket> itr =
                            ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();

                    while (itr.hasNext()) {
                        rcaTicket = itr.next();

                        if (rcaTicket.getTicketId().equals(rcaTicketHandlingStatus.getTicketId())) {
                            rcaTicket.setStatus(status);
                        }
                    }
                }
            } else if ("RT".equals(rcaTicketHandlingStatus.getTicketType())) {
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

                        if (StringUtils.isEmpty(rcaTicketHandlingStatus.getSopId())) {
                            sopId = ticketService.selectSopKey();
                            rcaTicketHandlingStatus.setSopId(sopId);
                        }
                        try {
                            LOGGER.info(">>>>>>>>> [RcaTicketHandlingService]  RT UpsertSop");
                            ticketService.upsertSop(rcaTicketHandlingStatus);
                        } catch (Exception e) {
                            LOGGER.error("RT UpsertSop err >>> " + e);
                        }
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
                    Iterator<RCATicket> itr =
                            ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();

                    while (itr.hasNext()) {
                        rcaTicket = itr.next();

                        if (rcaTicket.getTicketId().equals(rcaTicketHandlingStatus.getTicketId())) {
                            rcaTicket.setStatus(status);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTicketManager] ticketStatusModify() error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public synchronized void removeRcaTicket (String ticketId) {
        Iterator<RCATicket> itr;
        RCATicket rcaTicket;
        if (((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0) {
            itr = ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();
            while (itr.hasNext()) {
                rcaTicket = itr.next();
                if (ticketId.equals(rcaTicket.getTicketId())) {
                    itr.remove();
                }
            }
        }
    }

    @Override
    public void syslogAlarmStatusModify (RCATicketHandlingStatus rcaTicketHandlingStatus) {

        try {
            LOGGER.info(">>>>>>>>> [SyslogAlarmHandlingService]   syslogAlarmHandlingStatus : " + rcaTicketHandlingStatus.toString() + " <<<<<<<<<<<<<<<<<");
            HashMap<String, String> map = new HashMap<>();

            map.put("status", rcaTicketHandlingStatus.getStatus());
            map.put("alarmno", rcaTicketHandlingStatus.getAlarmno());


            ticketService.updateSyslogAlarmState(map);
            LOGGER.info(">>>>>>>>> [SyslogAlarmHandlingService] updateRcaTicketCurrentState map : " + map);
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[RcaTicketManager] syslogAlarmStatusModify() error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }


    }

    @Override
    public void syslogSopSave (RCATicketHandlingStatus rcaTicketHandlingStatus) {
        //운용자 수동조치 SOP

        try {
            HashMap<String, String> map = new HashMap<>();
            LOGGER.info(">>>>>>>>> [SyslogAlarmHandlingService]   syslogSopSave : " + rcaTicketHandlingStatus.toString() + " <<<<<<<<<<<<<<<<<");

            if ("IFMGR_IF_UP_4".equals(rcaTicketHandlingStatus.getAlarmmsg())) {
                map = new HashMap<>();
                map.put("alarmno", rcaTicketHandlingStatus.getAlarmno());
                map.put("faultClassify", rcaTicketHandlingStatus.getFaultClassify());
                map.put("faultType", rcaTicketHandlingStatus.getFaultType());
                map.put("faultDetailContent", rcaTicketHandlingStatus.getFaultDetailContent());
                map.put("etcContent", rcaTicketHandlingStatus.getEtcContent());
                map.put("status", rcaTicketHandlingStatus.getStatus());
                map.put("procStatus", "M");
                map.put("alarmOccurTime", String.valueOf(rcaTicketHandlingStatus.getAlarmtime()));
                map.put("handlingFinUser", "NIA ADMIN");
                map.put("alarmloc", rcaTicketHandlingStatus.getAlarmloc());
            } else if ("IFMGR_IF_DOWN_2".equals(rcaTicketHandlingStatus.getAlarmmsg())) {
                map = new HashMap<>();
                map.put("alarmno", rcaTicketHandlingStatus.getAlarmno());
                map.put("faultClassify", rcaTicketHandlingStatus.getFaultClassify());
                map.put("faultType", rcaTicketHandlingStatus.getFaultType());
                map.put("faultDetailContent", rcaTicketHandlingStatus.getFaultDetailContent());
                map.put("etcContent", rcaTicketHandlingStatus.getEtcContent());
                map.put("status", rcaTicketHandlingStatus.getStatus());
                map.put("procStatus", "M");
                map.put("alarmOccurTime", String.valueOf(rcaTicketHandlingStatus.getAlarmtime()));
                map.put("handlingFinUser", "NIA ADMIN");
                map.put("alarmloc", rcaTicketHandlingStatus.getAlarmloc());
            }

            autoProcessMapper.insertSyslogSop(map);


        } catch (Exception e) {

        }

    }
}
