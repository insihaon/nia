package com.nia.ip.sdn.syslog.linkage.mapper;


import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogCollectVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SyslogMapper {

    int selectSyslogSeq();
    void insertSyslogData(SyslogCollectVo syslogCollectVo);
    String selectAlarmLoc(int collectSeq);
}
