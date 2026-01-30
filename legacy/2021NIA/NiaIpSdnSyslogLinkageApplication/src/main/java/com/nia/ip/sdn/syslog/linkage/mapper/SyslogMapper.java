package com.nia.ip.sdn.syslog.linkage.mapper;


import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogCollectVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SyslogMapper {

    int selectCurrentSyslogSeq();
    int insertSyslogDataBatch(List<SyslogCollectVo> syslogCollectVoList);
    String selectAlarmLoc(int collectSeq);
}
