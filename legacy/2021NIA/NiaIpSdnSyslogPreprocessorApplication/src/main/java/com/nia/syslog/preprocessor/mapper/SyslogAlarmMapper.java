package com.nia.syslog.preprocessor.mapper;

import com.nia.syslog.preprocessor.vo.alarm.SysLogAlarmVo;
import com.nia.syslog.preprocessor.vo.resource.IpNodeInfoVo;
import com.nia.syslog.preprocessor.vo.rule.SyslogRuleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SyslogAlarmMapper {
    List<SyslogRuleVo> selectSyslogRule();

    IpNodeInfoVo selectNodeId(String nodeNm);

    void insertSyslogAlarm(SysLogAlarmVo sysLogAlarmVo);

    String selectAlarmLoc(@Param("alarmno") String alarmno, @Param("etc") String etc);

}
