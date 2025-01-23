package com.nia.syslog.preprocessor.service;

import com.nia.syslog.preprocessor.common.LoggerPrint;
import com.nia.syslog.preprocessor.mapper.SelfProcessSyslogMapper;
import com.nia.syslog.preprocessor.mapper.SyslogAlarmMapper;
import com.nia.syslog.preprocessor.vo.alarm.SysLogAlarmVo;
import com.nia.syslog.preprocessor.vo.resource.IpNodeInfoVo;
import com.nia.syslog.preprocessor.vo.selfProcess.AutoTreatProcessVo;
import com.nia.syslog.preprocessor.vo.selfProcess.SyslogAlarmVo;
import com.nia.syslog.preprocessor.vo.syslog.SyslogDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service("NiaSyslogAlarmHdlService")
public class SyslogAlarmHdlService {

    @Autowired
    @Qualifier("SyslogRuleHdlService")
    private SyslogRuleHdlService syslogRuleHdlService;

    @Autowired
    @Qualifier("SyslogMailingService")
    private SyslogMailingService syslogMailingService;

    @Autowired
    private SyslogAlarmMapper syslogAlarmMapper;

    @Autowired
    private SelfProcessSyslogMapper selfProcessSyslogMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SysLogAlarmVo> sysLogAlarmVoObjectFactory;


    public void niaSyslogHdlProcessor(SyslogDataVo pSyslogDataVo) {


        SyslogDataVo syslogDataVo;
        SysLogAlarmVo sysLogAlarmVo;
        AutoTreatProcessVo autoTreatProcessVo = null;
        HashMap<String, String> autoProcessSyslogMap;

        try {
            syslogDataVo = syslogRuleHdlService.occurRuleCheck(pSyslogDataVo);
            if (syslogDataVo.getSyslogRuleVo() != null) {

                sysLogAlarmVo = setSyslogAlarm(syslogDataVo);
                String alarmloc = syslogAlarmMapper.selectAlarmLoc(sysLogAlarmVo.getAlarmno(), sysLogAlarmVo.getEtc());
                LoggerPrint.infoLog("alarmloc chk : " + alarmloc + ", Alarmno chk : " + sysLogAlarmVo.getAlarmno());
                if (sysLogAlarmVo != null) {
                    sysLogAlarmVo.setAlarmloc(alarmloc);
                    syslogAlarmMapper.insertSyslogAlarm(sysLogAlarmVo);
                }

                // 자가처리
                autoProcessSyslogMap = new HashMap<>();
                autoProcessSyslogMap.put("selfProcessGroup", "ST");
                autoProcessSyslogMap.put("selfProcessType", "S");
                autoProcessSyslogMap.put("occur_time", String.valueOf(sysLogAlarmVo.getAlarmtime()));
                autoProcessSyslogMap.put("ticketId", null);
                autoProcessSyslogMap.put("ticketType", null);
                autoProcessSyslogMap.put("alarmno", sysLogAlarmVo.getAlarmno());

                selfProcessSyslogMapper.insertAutoProcess(autoProcessSyslogMap);

                SyslogAlarmVo row = selfProcessSyslogMapper.selectSyslogAlarmMst(sysLogAlarmVo.getAlarmno());
                //                 tb_syslog_alarm_mst 에 up이 들어왔을 경우 해당 장비 상태 전부 자동마감
                if ("IFMGR_IF_UP_4".equals(row.getAlarmmsg())) {
                    selfProcessSyslogMapper.updateSyslogPortAlarmStatus(sysLogAlarmVo.getNodeNum(),
                            sysLogAlarmVo.getNodeNm());
                    selfProcessSyslogMapper.updateAutoProcessSyslog(sysLogAlarmVo.getAlarmno());
                    autoProcessSyslogMap = new HashMap<>();
                    autoProcessSyslogMap.put("alarmno", sysLogAlarmVo.getAlarmno());
                    autoProcessSyslogMap.put("faultClassify", "포트장애");
                    autoProcessSyslogMap.put("faultType", "포트다운");
                    autoProcessSyslogMap.put("faultDetailContent", "포트리셋");
                    autoProcessSyslogMap.put("etcContent", "PORT UP");
                    autoProcessSyslogMap.put("status", "AUTO_FIN");
                    autoProcessSyslogMap.put("procStatus", "A");
                    autoProcessSyslogMap.put("alarmOccurTime", String.valueOf(sysLogAlarmVo.getAlarmtime()));
                    autoProcessSyslogMap.put("handlingFinUser", "NIA ADMIN");
                    autoProcessSyslogMap.put("alarmloc", alarmloc);
                } else if ("IFMGR_IF_DOWN_2".equals(row.getAlarmmsg())) {
                    autoProcessSyslogMap = new HashMap<>();
                    autoProcessSyslogMap.put("alarmno", sysLogAlarmVo.getAlarmno());
                    autoProcessSyslogMap.put("faultClassify", "포트장애");
                    autoProcessSyslogMap.put("faultType", "포트다운");
                    autoProcessSyslogMap.put("faultDetailContent", "포트리셋");
                    autoProcessSyslogMap.put("etcContent", "PORT DOWN");
                    autoProcessSyslogMap.put("status", "AUTO_FIN");
                    autoProcessSyslogMap.put("procStatus", "A");
                    autoProcessSyslogMap.put("alarmOccurTime", String.valueOf(sysLogAlarmVo.getAlarmtime()));
                    autoProcessSyslogMap.put("handlingFinUser", "NIA ADMIN");
                    autoProcessSyslogMap.put("alarmloc", alarmloc);
                } else if ("OSPF_OPR_LINK_UP_4".equals(row.getAlarmmsg())) {
                    selfProcessSyslogMapper.updateSyslogLinkAlarmStatus(sysLogAlarmVo.getNodeNum(),
                            sysLogAlarmVo.getNodeNm());
                    selfProcessSyslogMapper.updateAutoProcessSyslog(sysLogAlarmVo.getAlarmno());
                    autoProcessSyslogMap = new HashMap<>();
                    autoProcessSyslogMap.put("alarmno", sysLogAlarmVo.getAlarmno());
                    autoProcessSyslogMap.put("faultClassify", "링크장애");
                    autoProcessSyslogMap.put("faultType", "링크다운");
                    autoProcessSyslogMap.put("faultDetailContent", "링크리셋");
                    autoProcessSyslogMap.put("etcContent", "LINK UP");
                    autoProcessSyslogMap.put("status", "AUTO_FIN");
                    autoProcessSyslogMap.put("procStatus", "A");
                    autoProcessSyslogMap.put("alarmOccurTime", String.valueOf(sysLogAlarmVo.getAlarmtime()));
                    autoProcessSyslogMap.put("handlingFinUser", "NIA ADMIN");
                    autoProcessSyslogMap.put("alarmloc", alarmloc);
                    LoggerPrint.infoLog("OSPF_OPR_LINK_UP_4 : " + sysLogAlarmVo.getAlarmno());
                } else if ("OSPF_OPR_LINK_DOWN_4".equals(row.getAlarmmsg())) {
                    autoProcessSyslogMap = new HashMap<>();
                    autoProcessSyslogMap.put("alarmno", sysLogAlarmVo.getAlarmno());
                    autoProcessSyslogMap.put("faultClassify", "링크장애");
                    autoProcessSyslogMap.put("faultType", "링크다운");
                    autoProcessSyslogMap.put("faultDetailContent", "링크리셋");
                    autoProcessSyslogMap.put("etcContent", "LINK DOWN");
                    autoProcessSyslogMap.put("status", "AUTO_FIN");
                    autoProcessSyslogMap.put("procStatus", "A");
                    autoProcessSyslogMap.put("alarmOccurTime", String.valueOf(sysLogAlarmVo.getAlarmtime()));
                    autoProcessSyslogMap.put("handlingFinUser", "NIA ADMIN");
                    autoProcessSyslogMap.put("alarmloc", alarmloc);
                    LoggerPrint.infoLog("OSPF_OPR_LINK_DOWN_4 : " + sysLogAlarmVo.getAlarmno());
                }
                LoggerPrint.infoLog("[niaSyslogHdlProcessor] alarmloc chk : " + alarmloc);
                //                selfProcessSyslogMapper.updateAlarmloc(alarmloc, row.getAlarmno());
                selfProcessSyslogMapper.insertSyslogSop(autoProcessSyslogMap);

                if ("IFMGR_IF_DOWN_2".equals(row.getAlarmmsg())) {
                    syslogMailingService.syslogSendMail(sysLogAlarmVo);
                }


            }
        } catch (Exception e) {
            LoggerPrint.errorLog(e);
        }
    }

    private SysLogAlarmVo setSyslogAlarm(SyslogDataVo syslogDataVo) {
        LoggerPrint.infoLog("collectSeq : " + syslogDataVo.getCollectSeq());

        IpNodeInfoVo ipNodeInfoVo = null;
        SysLogAlarmVo sysLogAlarmVo = null;
        String ifNm;


        try {
            ipNodeInfoVo = syslogAlarmMapper.selectNodeId(syslogDataVo.getTags().getHostName().toLowerCase());

            if (ipNodeInfoVo != null) {

                sysLogAlarmVo = sysLogAlarmVoObjectFactory.getObject();

                //                sysLogAlarmVo.setRuleId(sysLogAlarmVo.getRuleId());
                sysLogAlarmVo.setRuleId(syslogDataVo.getSyslogRuleVo().getSyslogRuleId());
                sysLogAlarmVo.setAlarmno(String.valueOf(syslogDataVo.getCollectSeq()));
                sysLogAlarmVo.setAlarmtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(syslogDataVo.getTimestamp() * 1000L))));
                sysLogAlarmVo.setNodeNum(ipNodeInfoVo.getNodeNum());
                sysLogAlarmVo.setNodeNm(ipNodeInfoVo.getNodeNm());
                sysLogAlarmVo.setAlarmlvl(String.valueOf(syslogDataVo.getSyslogRuleVo().getAlarmSeverity()));
                sysLogAlarmVo.setAlarmmsg(syslogDataVo.getSyslogRuleVo().getSyslogRuleNm());
                sysLogAlarmVo.setEtc(syslogDataVo.getFields().getMessage());
                sysLogAlarmVo.setStatus("INIT");

                LoggerPrint.infoLog("setSyslogAlarm ====>" + sysLogAlarmVo);

            }
        } catch (Exception e) {
            LoggerPrint.errorLog(e);
        }
        return sysLogAlarmVo;
    }


}
