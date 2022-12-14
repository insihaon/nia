package com.nia.engine.mapper;

import com.nia.engine.vo.sdn.traffic.SdnTrafficInfoVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface SdnTrafficMapper {

    SdnTrafficInfoVo selectSdnTrafficAlarm(HashMap<String, String> map);

    void updateSdnTraffic(HashMap<String, String> parameterMap);
}
