package com.nia.syslog.preprocessor.service;

import com.nia.syslog.preprocessor.common.LoggerPrint;
import com.nia.syslog.preprocessor.mapper.SyslogAlarmMapper;
import com.nia.syslog.preprocessor.vo.rule.SyslogRuleVo;
import com.nia.syslog.preprocessor.vo.syslog.SyslogDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service("SyslogRuleHdlService")
public class SyslogRuleHdlService {

    @Autowired
    private SyslogAlarmMapper syslogAlarmMapper;

    protected SyslogDataVo occurRuleCheck(SyslogDataVo syslogDataVo){
        LoggerPrint.infoLog("collectSeq : "+ syslogDataVo.getCollectSeq());
        List<SyslogRuleVo> syslogRuleVoList = null;
        List<SyslogRuleVo> resultSyslogRuleVoList = null;
        ArrayList<SyslogRuleVo> syslogRuleVo;
        SyslogRuleVo minSyslogPriority;
        List<SyslogRuleVo> minSyslogPriority2;

        try {
            syslogRuleVoList = syslogAlarmMapper.selectSyslogRule();

            if(!CollectionUtils.isEmpty(syslogRuleVoList)){
                resultSyslogRuleVoList = syslogRuleVoList.stream()
                        .filter(x -> (StringUtils.isEmpty(x.getOccurStr1()) ? true : syslogDataVo.getFields().getMessage().contains(x.getOccurStr1()))
                                    && (StringUtils.isEmpty(x.getOccurStr2()) ? true : syslogDataVo.getFields().getMessage().contains(x.getOccurStr2()))
                                    && (StringUtils.isEmpty(x.getOccurStr3()) ? true : syslogDataVo.getFields().getMessage().contains(x.getOccurStr3()))
                                    && (StringUtils.isEmpty(x.getOccurExceptStr1()) ? true : !syslogDataVo.getFields().getMessage().contains(x.getOccurExceptStr1()))
                                    && (StringUtils.isEmpty(x.getOccurExceptStr2()) ? true : !syslogDataVo.getFields().getMessage().contains(x.getOccurExceptStr2()))
                                    && (StringUtils.isEmpty(x.getOccurExceptStr3()) ? true : !syslogDataVo.getFields().getMessage().contains(x.getOccurExceptStr3()))
//                                (!StringUtils.isEmpty(x.getOccurStr1()) && syslogDataVo.getFields().getMessage().contains(x.getOccurStr1()))
//                                    && (!StringUtils.isEmpty(x.getOccurStr2()) && syslogDataVo.getFields().getMessage().contains(x.getOccurStr2()))
//                                    && (!StringUtils.isEmpty(x.getOccurStr3()) && syslogDataVo.getFields().getMessage().contains(x.getOccurStr3()))
//                                    && (!StringUtils.isEmpty(x.getOccurExceptStr1()) && !syslogDataVo.getFields().getMessage().contains(x.getOccurExceptStr1()))
//                                    && (!StringUtils.isEmpty(x.getOccurExceptStr2()) && !syslogDataVo.getFields().getMessage().contains(x.getOccurExceptStr2()))
//                                    && (!StringUtils.isEmpty(x.getOccurExceptStr3()) && !syslogDataVo.getFields().getMessage().contains(x.getOccurExceptStr3()))
                        )
                        .collect(Collectors.toList());

            }

            Comparator<SyslogRuleVo> comparatorByPriority = Comparator.comparingInt(SyslogRuleVo::getPriorityOrder);
            minSyslogPriority = syslogRuleVoList.stream()
                    .min(comparatorByPriority)
                    .orElseThrow(NoSuchElementException::new);

            if(!CollectionUtils.isEmpty(resultSyslogRuleVoList)){
                LoggerPrint.infoLog("sysRuleId : " + resultSyslogRuleVoList.get(0).getSyslogRuleId());
                syslogDataVo.setSyslogRuleVo(minSyslogPriority);

            }
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
        return syslogDataVo;
    }
}
