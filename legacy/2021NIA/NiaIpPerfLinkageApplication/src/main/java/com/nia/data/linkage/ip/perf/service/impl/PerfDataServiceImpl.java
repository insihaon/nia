package com.nia.data.linkage.ip.perf.service.impl;

import com.nia.data.linkage.ip.perf.mapper.linkage.LinkagePerfMapper;
import com.nia.data.linkage.ip.perf.mapper.nia.NiaPerfMapper;
import com.nia.data.linkage.ip.perf.service.PerfDataService;
import com.nia.data.linkage.ip.perf.vo.perf.PerfVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("PerfDataService")
public class PerfDataServiceImpl implements PerfDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerfDataServiceImpl.class);

    @Autowired
    private LinkagePerfMapper linkageAlarmMapper;

    @Autowired
    private NiaPerfMapper niaAlarmMapper;

    @Override
    public void getPerfData() {
        LOGGER.info("==========>[PerfDataService] getPerfData <==============");

        String inttimestamp = null;

        ArrayList<PerfVo> perfVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            inttimestamp = niaAlarmMapper.selectPerfYdKey("ipPerfKey");

            if(StringUtils.isNotEmpty(inttimestamp)){
                perfVoList = linkageAlarmMapper.selectPerfList(Long.parseLong(inttimestamp));

                if(perfVoList != null && perfVoList.size() > 0) {

                    LOGGER.info("==========>[PerfDataService] getPerfData perfVoList("+perfVoList.size() +") <==============");
                    objectHashMap = new HashMap<>();
                    objectHashMap.put("perfVoList", perfVoList);
                    niaAlarmMapper.insertPerf(objectHashMap);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipPerfKey");
                    strHashMap.put("value", perfVoList.get(perfVoList.size()-1).getIntTimestamp()+"");
                    niaAlarmMapper.updatePerfYdKey(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerfDataService] getPerfData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

}
