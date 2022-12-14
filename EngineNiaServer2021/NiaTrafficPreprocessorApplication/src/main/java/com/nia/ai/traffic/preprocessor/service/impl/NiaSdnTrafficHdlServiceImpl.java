package com.nia.ai.traffic.preprocessor.service.impl;

import com.nia.ai.traffic.preprocessor.amqp.EngineSendPrdAmqp;
import com.nia.ai.traffic.preprocessor.mapper.TrafficMapper;
import com.nia.ai.traffic.preprocessor.service.NiaSdnTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.EngineTrafficeResultVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.traffic.SdnTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.traffic.SdnTrafficVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("NiaSdnTrafficHdlService")
public class NiaSdnTrafficHdlServiceImpl implements NiaSdnTrafficHdlService {
    private final static Logger LOGGER = Logger.getLogger(NiaSdnTrafficHdlService.class);

    @Autowired
    private TrafficMapper trafficMapper;

    @Autowired
    private EngineSendPrdAmqp engineSendPrdAmqp;

    @Autowired
    private EngineTrafficeResultVo engineTrafficeResultVo;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EngineTrafficeResultVo> engineTrafficeResultVoObjectFactory;

    public void niaSdnTrafficeHdlProcessor(SdnTrafficListVo sdnTrafficListVo) {
        LOGGER.info("==========>[niaSdnTrafficeHdlService] niaSdnTrafficeHdlProcessor <==============");

        HashMap<String, String> hashMap;
        String trafficGb = null;

        ArrayList<SdnTrafficVo> sdnTrafficVoList;
        int cnt = 0;
        int insertCnt = 0;

        try {
            if(sdnTrafficListVo != null){
                if(sdnTrafficListVo.getData() != null && sdnTrafficListVo.getData().size() > 0){
                    LOGGER.info("==========>[niaSdnTrafficeHdlService] niaSdnTrafficeHdlProcessor size : "+sdnTrafficListVo.getData().size()+"<==============");
                    sdnTrafficVoList = new ArrayList<>();

                    for(SdnTrafficVo sdnTrafficVo : sdnTrafficListVo.getData()){
                        if (StringUtils.isNotEmpty(sdnTrafficVo.getSdn_node_id()) && StringUtils.isEmpty(sdnTrafficVo.getStrifid())){
                            trafficGb = "Traffic_N";
                        }
                        if (StringUtils.isEmpty(sdnTrafficVo.getSdn_node_id()) && StringUtils.isNotEmpty(sdnTrafficVo.getStrifid())){
                            trafficGb = "Traffic_P";
                        }
                        if(sdnTrafficVo.isFltbpsin_anomaly() || sdnTrafficVo.isFltbpsout_anomaly()){
                            hashMap = new HashMap<>();
                            hashMap.put("strifid", sdnTrafficVo.getStrifid());
                            hashMap.put("measured_datetime", String.valueOf(sdnTrafficVo.getMeasured_datetime()));
                            hashMap.put("trafficGb", trafficGb);
                            hashMap.put("fltbpsinAnomaly", String.valueOf(sdnTrafficVo.isFltbpsin_anomaly()));
                            hashMap.put("fltbpsoutAnomaly", String.valueOf(sdnTrafficVo.isFltbpsout_anomaly()));

                            cnt = trafficMapper.selectSdnTrafficCheck(hashMap);

                            LOGGER.info("==========>[niaSdnTrafficeHdlService] niaSdnTrafficeHdlProcessor check cnt : "+cnt+"<==============");


                            if(cnt < 1){

                                sdnTrafficVo.setTraffic_gb(trafficGb);


                                insertCnt = trafficMapper.insertSdnTraffic(hashMap);
                                if (insertCnt > 0){
                                    trafficMapper.insertAiSdnTraffic(sdnTrafficVo);

                                    sdnTrafficVoList.add(sdnTrafficVo);
                                    LOGGER.info("==========>[niaSdnTrafficeHdlService] niaSdnTrafficeHdlProcessor add : "+sdnTrafficVo.toString()+"<==============");
                                }

                            }

                            if(sdnTrafficVoList != null && sdnTrafficVoList.size() > 0){
                                sdnTrafficListVo.setData(sdnTrafficVoList);
                                engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
                                    engineTrafficeResultVo.setGb(trafficGb);
                                engineTrafficeResultVo.setTrafficListVo(sdnTrafficListVo);
                                engineSendPrdAmqp.sendMessageCmd(engineTrafficeResultVo);
                            }
                        }
                    }

                    hashMap = null;


//                    if(sdnTrafficVoList != null && sdnTrafficVoList.size() > 0){
//                        sdnTrafficListVo.setData(sdnTrafficVoList);
//                        engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
//                        engineTrafficeResultVo.setGb("traffic");
//                        engineTrafficeResultVo.setTrafficListVo(sdnTrafficListVo);
//
//                        engineSendPrdAmqp.sendMessageCmd(engineTrafficeResultVo);
//                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [niaSdnTrafficeHdlService] niaSdnTrafficeHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
