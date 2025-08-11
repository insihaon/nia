package com.nia.engine.service.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.mapper.AutoProcessMapper;
import com.nia.engine.service.RcaTicketManagerService;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.*;
import com.nia.engine.vo.selfProcess.AutoTreatProcessVo;
import com.nia.engine.vo.syslog.SyslogAlarmVo;
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
    private AutoProcessMapper autoProcessMapper;

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

    @Autowired
    private SelfProcessService selfProcessService;

    public void clearTicketCheck(){
        /*
        * 2025-07-10 고성호
        * (추측) 사용자가 UI에서 수동마감했을때
        * 상태가 변경된 것을 확인해서 Queue를 통해 UI로 변경되었다고 알리는 기능으로 보임
        * (이상한점) 왜?
        * */
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
        /*
        *
        * */
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

    public void TicketClearPassing5Minute(){ //자가최적화 5분 지난 티켓 자동 클리어 & 포트다운
        /*
        * 2025-07-10 고성호
        * 티켓을 마감 처리하는 것이면 자가 최적화가 아니라 자동마감.. 같은데..
        * 그리고 자동마감이라면 포트다운이 아니라 포트 업이 맞는 것 같은데..
        * */
//        시험 전 까지는 48시간 주기 마감.
        List<RCATicket> rcaTicketList = null;
        HashMap<String,String> map;
        HashMap<String, String> updateMap;
        String sopId = "";
        try {
            rcaTicketList = ticketService.selectRca48Ticket();

            if (rcaTicketList.size() > 0){
                map = new HashMap<>();
                for (RCATicket ticketList : rcaTicketList){

                    map.put("ticketId",ticketList.getTicketId());
                    map.put("status",RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                    //tb_ticket_current 업데이트
                    ticketService.updateRcaTicketStatus(map);

                    //tb_sop 저장
                    map = new HashMap<>();
                    sopId = ticketService.selectSopKey();
                    map.put("sopId",sopId);
                    map.put("ticketId", ticketList.getTicketId());
                    map.put("ticketType", ticketList.getTicketType());
                    map.put("ticketResult", ticketList.getTicketRcaResultDtlCode());
                    map.put("status", "AUTO_FIN");
                    map.put("detail", "");
                    map.put("faultClassify", "-");
                    map.put("faultType", "-");
                    map.put("faultDetailContent", "-");
                    map.put("etcContent", "");
                    map.put("aiAccuracy", "0");
                    map.put("faultTypeContent", null);
                    map.put("startTime", null);
                    map.put("endTime", null);
                    if (ticketList.getStatus().equals("ACK")){
                        map.put("handlingAckUser", "NIA ADMIN");
                    }else{
                        map.put("handlingAckUser", null);
                    }
                    if (ticketList.getTicketType().equals("ATT2")){
                        map.put("autoprocType", "SOA");
                    }
                    if (ticketList.getTicketType().equals("NTT")){
                        map.put("autoprocType", "SON");
                    }
                    map.put("zero1Model",ticketList.getZero1Model());
                    map.put("zero1Entropy",ticketList.getZero1Entropy());

                    autoProcessMapper.insertAutoProcessSop(map);

                    LOGGER.info(">>>>>>>>>clear 5-min-old traffic ticket : " + ticketList.getTicketId() +"<<<<<<<<<<<<<<");
                    TicketPortDownCommand();
                    LOGGER.info(">>>>>>>>>tb_sop insert auto_fin : " + map.get("ticketId") +"<<<<<<<<<<<<<<");

                }
            }

        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[EngineClearSchedulerJobServiceImpl] TicketClearPassing5Minute() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }

    }
// 포트다운 시험때까지 주석 스케줄러도 주석 풀어야함
    public void TicketPortDownCommand(){
//        String serviceUrl = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/shutdown?nodename=daejeon-5812&ifname=xe13";
        String serviceUrl = "";
        List<AutoTreatProcessVo> autoTreatProcessVoList;

        try {
            autoTreatProcessVoList = autoProcessMapper.selectCommandtoTicketList();

            if (autoTreatProcessVoList.size() > 0 && autoTreatProcessVoList != null ) {
                for (AutoTreatProcessVo autoTreatProcessVo : autoTreatProcessVoList) {
                    if (autoTreatProcessVo.getTicketId() != null) {
                        LOGGER.info("TicketPortDownCommand() port down =====> " + autoTreatProcessVo.getTicketId());
                        selfProcessService.UserTicketFeedbackCheck(serviceUrl, autoTreatProcessVo.getTicketId());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[EngineClearSchedulerJobServiceImpl] TicketPortDownCommand() error: " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }

    }



    public void SyslogClearPassing5MinuteAndRemoteCommand(){ //자가회복
        List<SyslogAlarmVo> syslogAlarmVoList = null;
        HashMap<String,String> map;
        HashMap<String, String> updateMap;
        try {
            syslogAlarmVoList = ticketService.selectRcaBefor5mSyslog();

            if (syslogAlarmVoList.size() > 0){
                map = new HashMap<>();
                for (SyslogAlarmVo ticketList : syslogAlarmVoList){

                    map.put("alarmno",ticketList.getAlarmno());
                    map.put("status",RcaCodeInfo.TICKET_STATUS_AUTO_FIN);

                    //tb_ticket_current 업데이트
                    ticketService.updateRcaTicketStatus(map);

                    AutoTreatProcessVo autoTreatProcessVo = autoProcessMapper.selectAutoProcess(ticketList.getAlarmno());

                    //tb_syslog_sop 등록
                    map = new HashMap<>();
                    if ("IFMGR_IF_UP_4".equals(ticketList.getAlarmmsg())){
                        map = new HashMap<>();
                        map.put("alarmno",ticketList.getAlarmno());
                        map.put("faultClassify","포트장애");
                        map.put("faultType","포트다운");
                        map.put("faultDetailContent","포트리셋");
                        map.put("etcContent","PORT UP");
                        map.put("status","AUTO_FIN");
                        map.put("procStatus","A");
                        map.put("alarmOccurTime", String.valueOf(ticketList.getAlarmtime()));
                        map.put("handlingFinUser","NIA ADMIN");
                        map.put("alarmloc",ticketList.getAlarmloc());
                    }else if("IFMGR_IF_DOWN_2".equals(ticketList.getAlarmmsg())){
                        map = new HashMap<>();
                        map.put("alarmno",ticketList.getAlarmno());
                        map.put("faultClassify","포트장애");
                        map.put("faultType","포트다운");
                        map.put("faultDetailContent","포트리셋");
                        map.put("etcContent","PORT DOWN");
                        map.put("status","AUTO_FIN");
                        map.put("procStatus","A");
                        map.put("alarmOccurTime", String.valueOf(ticketList.getAlarmtime()));
                        map.put("handlingFinUser","NIA ADMIN");
                        map.put("alarmloc",ticketList.getAlarmloc());
                    }





                    LOGGER.info(">>>>>>>>>clear 5-minutes-old syslog : " + ticketList.getAlarmno() +"<<<<<<<<<<<<<<");

//                    String shutdownUrl = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/shutdown?nodename=daejeon-5812&ifname=xe13";
//                    selfProcessService.UserSyslogFeedbackCheck(shutdownUrl,autoTreatProcessVo.getAlarmno());
//                    LOGGER.info(">>>>>>>>>syslog shutdown : " + autoTreatProcessVo.getAlarmno() +"<<<<<<<<<<<<<<");
//                    String noshutUrl = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/noshut?nodename=daejeon-5812&ifname=xe13";
//                    selfProcessService.UserSyslogFeedbackCheck(noshutUrl,autoTreatProcessVo.getAlarmno());
//                    LOGGER.info(">>>>>>>>>syslog noshut about : " + autoTreatProcessVo.getAlarmno() +"<<<<<<<<<<<<<<");

                }
            }

        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[EngineClearSchedulerJobServiceImpl] SyslogClearPassing5MinuteAndRemoteCommand() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }

    }

    public void SyslogPortDownCommand(){
        //시스로그 알람 테이블 AUTO_FIN 이고, 자가처리 테이블에서 N인 데이터들 원격제어
//        String shutdownUrl = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/shutdown?nodename=daejeon-5812&ifname=xe13";
//        String noshut = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/noshut?nodename=daejeon-5812&ifname=xe13";
        String shutdownUrl = "";
        String noshut = "";
        List<AutoTreatProcessVo> autoTreatProcessVoList;

        try {
            autoTreatProcessVoList = autoProcessMapper.selectCommandtoSyslogList();

            if (autoTreatProcessVoList.size() > 0 && autoTreatProcessVoList != null ) {
                for (AutoTreatProcessVo autoTreatProcessVo : autoTreatProcessVoList) {
                    if (autoTreatProcessVo.getTicketId() != null) {
                        LOGGER.info("TicketPortDownCommand() port down =====> " + autoTreatProcessVo.getTicketId());
                        selfProcessService.UserSyslogFeedbackCheck(shutdownUrl, autoTreatProcessVo.getTicketId());
                        LOGGER.info("TicketPortDownCommand() port up =====> " + autoTreatProcessVo.getTicketId());
                        selfProcessService.UserSyslogFeedbackCheck(noshut, autoTreatProcessVo.getTicketId());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[EngineClearSchedulerJobServiceImpl] TicketPortDownCommand() error: " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }

    }

}