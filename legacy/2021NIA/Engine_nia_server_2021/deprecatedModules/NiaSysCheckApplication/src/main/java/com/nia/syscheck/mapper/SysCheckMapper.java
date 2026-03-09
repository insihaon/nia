package com.nia.syscheck.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface SysCheckMapper {

    /* Monitoring */
    String selectMonitoringResult(HashMap<String,String> map);

    /* Engine */
    void insertEngineSystemStatus(HashMap<String,String> map);


    /* Sflow */
    String selectSflowLastDataTime(HashMap<String,String> map);
    void updateSflowLastDataTime(HashMap<String,String> map);
}
