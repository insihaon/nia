/*
* gather.tb_sflow_collect에 데이터가 적재되지 않는 현상 방지 서비스
* */

package com.nia.syscheck.service.impl;

import com.nia.syscheck.mapper.SysCheckMapper;
import com.nia.syscheck.service.SysCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Service("SflowSysCheckService")
public class SflowSysCheckServiceImpl implements SysCheckService {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(SflowSysCheckServiceImpl.class));

    @Autowired
    private SysCheckMapper sysCheckMapper;

    @Override
    public void sysCheck() throws IOException, TimeoutException {
        String code = "test";
        String name = "sflow";
        String result = null;


        LOGGER.info(">>>>> [ SysCheckService 'Sflow' ] <<<<<");
        // 0.모니터링 기존 result_content값을 조회한다.
            // sflow 데이터량이 많아 일반 조회로는 성능이 떨어져 조건절을 주기 위함.
        HashMap<String,String> resultMap = new HashMap<>();
        resultMap.put("code",code);
        resultMap.put("name",name);
        String resultContent = sysCheckMapper.selectMonitoringResult(resultMap);

        // 1.sflow 데이터 조회한다.
        HashMap<String,String> lasttimeMap = new HashMap<>();
        lasttimeMap.put("lasttime",resultContent);
        String lastTimeData = sysCheckMapper.selectSflowLastDataTime(lasttimeMap);

        // 2. sflow데이터의 가장 최근 시간이 현재 시간이 5분 이상 차이나면 F , 아니면 S
        OffsetDateTime currentTime = OffsetDateTime.now();
        OffsetDateTime OffsetLastTimeData = OffsetDateTime.parse(lastTimeData);
        long diffTime = java.time.Duration.between(OffsetLastTimeData,currentTime).getSeconds();

        if (diffTime >= 300) { // 5분 이상
            result = "F";
        } else {
            result = "S";
        }

        LOGGER.info(">>>>> [ SysCheckService 'Sflow' ] result : " + result + " <<<<<");

        // 3.마지막 collect_timestamp 값을 map에 넣는다. (그 외 map도)
        HashMap<String,String> updateMap = new HashMap<>();
        updateMap.put("resultContent",lastTimeData);
        updateMap.put("result",result);
        updateMap.put("code",code);
        updateMap.put("name",name);

        // 4.모니터링 테이블에 update 한다.
        sysCheckMapper.updateSflowLastDataTime(updateMap);
        LOGGER.info(">>>>> [ SysCheckService 'Sflow' ] update <<<<<");

    }
}
