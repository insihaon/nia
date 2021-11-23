package com.nia.ai.traffic.preprocessor.service.impl;

import com.nia.ai.traffic.preprocessor.amqp.EngineSendPrdAmqp;
import com.nia.ai.traffic.preprocessor.mapper.TrafficMapper;
import com.nia.ai.traffic.preprocessor.service.NiaAnomalousTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.EngineTrafficeResultVo;
import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**

 * @author
 *
 */
@Service("NiaAnomalousTrafficHdlService")
public class NiaAnomalousTrafficHdlServiceImpl implements NiaAnomalousTrafficHdlService {
    private final static Logger LOGGER = Logger.getLogger(NiaAnomalousTrafficHdlService.class);

    @Autowired
    private TrafficMapper trafficMapper;

    @Autowired
    private EngineSendPrdAmqp engineSendPrdAmqp;

    @Autowired
    private EngineTrafficeResultVo engineTrafficeResultVo;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EngineTrafficeResultVo> engineTrafficeResultVoObjectFactory;

    @Override
    public void niaAnomalousTrafficeHdlProcessor(AnomalousTrafficListVo anomalousTrafficListVo) {
        LOGGER.info("==========>[NiaAnomalousTrafficHdlService] niaAnomalousTrafficeHdlProcessor <==============");

        HashMap<String, String> hashMap;

        try {
            if(anomalousTrafficListVo != null){
                if(anomalousTrafficListVo.getData() != null && anomalousTrafficListVo.getData().size() > 0){
                    LOGGER.info("==========>[NiaAnomalousTrafficHdlService] niaAnomalousTrafficeHdlProcessor size : "+anomalousTrafficListVo.getData().size()+"<==============");

                    for(AnomalousTrafficVo anomalousTrafficVo : anomalousTrafficListVo.getData()){
                        hashMap = new HashMap<>();
                        hashMap.put("strifid", anomalousTrafficVo.getStrifid());
                        hashMap.put("inttimestamp", String.valueOf(anomalousTrafficVo.getInttimestamp()));
                        trafficMapper.insertAnomalousTraffic(hashMap);
                    }

                    hashMap = null;

                    engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
                    engineTrafficeResultVo.setGb("anomalous");
                    engineTrafficeResultVo.setPerfListVo(anomalousTrafficListVo);

                    engineSendPrdAmqp.sendMessageCmd(engineTrafficeResultVo);
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [NiaAnomalousTrafficHdlService] niaAnomalousTrafficeHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
