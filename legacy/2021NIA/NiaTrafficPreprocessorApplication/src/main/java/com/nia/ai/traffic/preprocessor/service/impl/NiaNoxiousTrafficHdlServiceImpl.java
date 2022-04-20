package com.nia.ai.traffic.preprocessor.service.impl;

import com.nia.ai.traffic.preprocessor.amqp.EngineSendPrdAmqp;
import com.nia.ai.traffic.preprocessor.mapper.TrafficMapper;
import com.nia.ai.traffic.preprocessor.service.NiaNoxiousTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.EngineTrafficeResultVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrfficVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;


/**

 * @author
 *
 */
@Service("NiaNoxiousTrafficHdlService")
public class NiaNoxiousTrafficHdlServiceImpl implements NiaNoxiousTrafficHdlService {
    private final static Logger LOGGER = Logger.getLogger(NiaNoxiousTrafficHdlService.class);

    @Autowired
    private TrafficMapper trafficMapper;

    @Autowired
    private EngineSendPrdAmqp engineSendPrdAmqp;

    @Autowired
    private EngineTrafficeResultVo engineTrafficeResultVo;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EngineTrafficeResultVo> engineTrafficeResultVoObjectFactory;

    @Override
    public void niaNoxiousTrafficeHdlProcessor(NoxiousTrafficListVo noxiousListVo) {
        LOGGER.info("==========>[NiaNoxiousTrafficHdlService] niaNoxiousTrafficeHdlProcessor <==============");

        HashMap<String, String> hashMap;
        ArrayList<NoxiousTrfficVo> noxiousTrfficVoList;

        try {
            if(noxiousListVo != null){
                if(noxiousListVo.getData() != null && noxiousListVo.getData().size() > 0){
                    LOGGER.info("==========>[NiaNoxiousTrafficHdlService] niaNoxiousTrafficeHdlProcessor size : "+noxiousListVo.getData().size()+"<==============");
                    noxiousTrfficVoList = new ArrayList<>();

                    for(NoxiousTrfficVo noxiousTrfficVo : noxiousListVo.getData()){
                        if(noxiousTrfficVo.getAnomaly() == 1){
                            hashMap = new HashMap<>();
                            hashMap.put("strresip", noxiousTrfficVo.getStrresip());
                            hashMap.put("strsIp", noxiousTrfficVo.getStrs_ip());
                            hashMap.put("strsPort", String.valueOf(noxiousTrfficVo.getStrs_port()));
                            hashMap.put("strdIp", noxiousTrfficVo.getStrd_ip());
                            hashMap.put("strdPort", String.valueOf(noxiousTrfficVo.getStrd_port()));
                            hashMap.put("dateregdate", String.valueOf(noxiousTrfficVo.getDateregdate()));
                            trafficMapper.insertNoxiousTraffic(hashMap);

                            noxiousTrfficVoList.add(noxiousTrfficVo);
                            LOGGER.info("==========>[NiaNoxiousTrafficHdlService] niaNoxiousTrafficeHdlProcessor add : "+noxiousTrfficVo.toString()+"<==============");

                        }
                    }

                    hashMap = null;

                    if(noxiousTrfficVoList != null && noxiousTrfficVoList.size() > 0){
                        noxiousListVo.setData(noxiousTrfficVoList);
                        engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
                        engineTrafficeResultVo.setGb("noxious");
                        engineTrafficeResultVo.setNoxiousListVo(noxiousListVo);

                        engineSendPrdAmqp.sendMessageCmd(engineTrafficeResultVo);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [NiaNoxiousTrafficHdlService] niaNoxiousTrafficeHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
