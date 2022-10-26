package com.nia.ip.sdn.syslog.linkage.mapper;


import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogDataVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SyslogMapper {

    int selectSyslogSeq();
    void insertSyslogData(SyslogDataVo syslogDataVo);
}
