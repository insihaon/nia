package com.nia.ip.syslog.linkage.mapper;

import com.nia.ip.syslog.linkage.vo.syslog.SyslogListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface SyslogMapper {

    String selectIpYbNumKey();
    void insertSyslogYd(SyslogListVo syslogListVo);
    void insertSyslogData(HashMap<String, Object> map);
}
