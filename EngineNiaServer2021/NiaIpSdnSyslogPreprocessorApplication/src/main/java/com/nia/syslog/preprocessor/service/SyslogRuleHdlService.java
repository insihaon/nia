package com.nia.syslog.preprocessor.service;

import com.nia.syslog.preprocessor.common.LoggerPrint;
import com.nia.syslog.preprocessor.mapper.SyslogAlarmMapper;
import com.nia.syslog.preprocessor.vo.rule.SyslogRuleVo;
import com.nia.syslog.preprocessor.vo.syslog.SyslogDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("SyslogRuleHdlService")
public class SyslogRuleHdlService {

    @Autowired
    private SyslogAlarmMapper syslogAlarmMapper;

    protected SyslogDataVo occurRuleCheck(SyslogDataVo syslogDataVo){
        List<SyslogRuleVo> syslogRuleVoList = null;
        List<SyslogRuleVo> resultSyslogRuleVoList = null;

        try {
            syslogRuleVoList = syslogAlarmMapper.selectSyslogRule();

            if(!CollectionUtils.isEmpty(syslogRuleVoList)){
                resultSyslogRuleVoList = syslogRuleVoList.stream()
//                        .filter(x -> Integer.parseInt(x.getAlarmSeverity()) == syslogDataVo.getFields().getSeverityCode())
                        .filter(x -> syslogDataVo.getFields().getMessage().contains(x.getOccurStr1()))
                        .filter(x -> StringUtils.isEmpty(x.getOccurStr2()) && syslogDataVo.getFields().getMessage().contains(x.getOccurStr2()))
                        .filter(x -> StringUtils.isEmpty(x.getOccurStr3()) && syslogDataVo.getFields().getMessage().contains(x.getOccurStr3()))
                        .collect(Collectors.toList());
            }

            if(!CollectionUtils.isEmpty(resultSyslogRuleVoList)){
                syslogDataVo.setSyslogRuleVo(resultSyslogRuleVoList.get(0));
            }
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
        return syslogDataVo;
    }
}
