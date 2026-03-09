package com.nia.syslog.preprocessor.mapper;

import com.nia.syslog.preprocessor.vo.selfProcess.AutoTreatProcessVo;
import com.nia.syslog.preprocessor.vo.selfProcess.SyslogAlarmVo;
import com.nia.syslog.preprocessor.vo.selfProcess.SyslogSopVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

@Mapper
public interface SelfProcessSyslogMapper {

    AutoTreatProcessVo selectAutoProcess(String num);

    void insertAutoProcess(HashMap<String, String> map);

    SyslogAlarmVo selectSyslogAlarmMst(String num);

    void updateSyslogPortAlarmStatus(@Param("nodeNum") String nodeNum, @Param("nodeNm") String nodeNm);

    void updateSyslogLinkAlarmStatus(@Param("nodeNum") String nodeNum, @Param("nodeNm") String nodeNm);

    void updateAutoProcessSyslog(String alarmno);

    void insertSyslogSop(HashMap<String, String> map);

    //    void updateAlarmloc(String alarmno);
    // Mapper Interface
    void updateAlarmloc(@Param("alarmloc") String alarmloc, @Param("alarmno") String alarmno);

    SyslogSopVo selectSyslogSop(String alarmno);


}
