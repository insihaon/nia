package com.nia.syslog.preprocessor.service;

import com.nia.syslog.preprocessor.common.LoggerPrint;
import com.nia.syslog.preprocessor.mapper.SyslogAlarmMapper;
import com.nia.syslog.preprocessor.vo.alarm.SysLogAlarmVo;
import com.nia.syslog.preprocessor.vo.resource.IpNodeInfoVo;
import com.nia.syslog.preprocessor.vo.rule.SyslogRuleVo;
import com.nia.syslog.preprocessor.vo.syslog.SyslogDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("NiaSyslogAlarmHdlService")
public class SyslogAlarmHdlService {

    @Autowired
    @Qualifier("SyslogRuleHdlService")
    private SyslogRuleHdlService syslogRuleHdlService;

    @Autowired
    private SyslogAlarmMapper syslogAlarmMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SysLogAlarmVo> sysLogAlarmVoObjectFactory;


    public void niaSyslogHdlProcessor(SyslogDataVo pSyslogDataVo) {
        LoggerPrint.infoLog("collectSeq : "+ pSyslogDataVo.getCollectSeq());

        SyslogDataVo syslogDataVo;
        SysLogAlarmVo sysLogAlarmVo;

        try {
            syslogDataVo = syslogRuleHdlService.occurRuleCheck(pSyslogDataVo);

            if(syslogDataVo.getSyslogRuleVo() != null){
                sysLogAlarmVo = setSyslogAlarm(syslogDataVo);

                if(sysLogAlarmVo != null){
                    syslogAlarmMapper.insertSyslogAlarm(sysLogAlarmVo);
                }
            }
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
    }

    private SysLogAlarmVo setSyslogAlarm(SyslogDataVo syslogDataVo){
        LoggerPrint.infoLog("collectSeq : "+ syslogDataVo.getCollectSeq());

        IpNodeInfoVo ipNodeInfoVo = null;
        SysLogAlarmVo sysLogAlarmVo = null;

        try {
            ipNodeInfoVo = syslogAlarmMapper.selectNodeId(syslogDataVo.getTags().getHostName());

            if(ipNodeInfoVo != null){
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
            }
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
        return sysLogAlarmVo;
    }

}
