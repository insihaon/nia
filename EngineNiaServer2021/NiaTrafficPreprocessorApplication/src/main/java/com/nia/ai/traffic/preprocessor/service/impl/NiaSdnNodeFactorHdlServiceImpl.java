package com.nia.ai.traffic.preprocessor.service.impl;

import com.nia.ai.traffic.preprocessor.amqp.EngineSendPrdAmqp;
import com.nia.ai.traffic.preprocessor.mapper.TrafficMapper;
import com.nia.ai.traffic.preprocessor.service.NiaNoxiousTrafficHdlService;
import com.nia.ai.traffic.preprocessor.service.NiaSdnNodeFactorHdlService;
import com.nia.ai.traffic.preprocessor.vo.EngineTrafficeResultVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.factor.NodeFactorListVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.factor.NodeFactorVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**

 * @author
 *
 */
@Service("NiaSdnNodeFactorHdlService")
public class NiaSdnNodeFactorHdlServiceImpl implements NiaSdnNodeFactorHdlService {
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
    public void niaSdnNodeFactorHdlProcessor(NodeFactorListVo nodeFactorListVo) {
        LOGGER.info("==========>[NiaSdnNodeFactorHdlService] niaSdnNodeFactorHdlProcessor <==============");

        HashMap<String, String> hashMap;
        ArrayList<NodeFactorVo> nodeFactorVoList;
        int cnt = 0;
        int insertCnt = 0;

        try {
            if(nodeFactorListVo != null){
                if(nodeFactorListVo.getData() != null && nodeFactorListVo.getData().size() > 0){
                    LOGGER.info("==========>[NiaSdnNodeFactorHdlService] niaSdnNodeFactorHdlProcessor size : "+nodeFactorListVo.getData().size()+"<==============");
                    nodeFactorVoList = new ArrayList<>();

                    for(NodeFactorVo nodeFactorVo : nodeFactorListVo.getData()){
                        if(nodeFactorVo.getId() != null){
                            hashMap = new HashMap<>();
                            hashMap.put("measured_datetime", String.valueOf(nodeFactorVo.getMeasured_datetime()));
                            hashMap.put("id", nodeFactorVo.getId());

                            cnt = trafficMapper.selectNodeFactorCheck(hashMap);

                            LOGGER.info("==========>[NiaSdnNodeFactorHdlService] niaSdnNodeFactorHdlProcessor check cnt : "+cnt+"<==============");

                            if(cnt < 1){
                                insertCnt = trafficMapper.insertNodeFactor(hashMap);
                                if (insertCnt > 0){
                                    trafficMapper.insertAiNodeFactor(nodeFactorVo);
                                    nodeFactorVoList.add(nodeFactorVo);
                                    LOGGER.info("==========>[NiaSdnNodeFactorHdlService] niaSdnNodeFactorHdlProcessor add : "+ nodeFactorVo.toString()+"<==============");
                                }

                            }
                        }
                    }

                    hashMap = null;

                    if(nodeFactorVoList != null && nodeFactorVoList.size() > 0){
                        nodeFactorListVo.setData(nodeFactorVoList);
                        engineTrafficeResultVo = engineTrafficeResultVoObjectFactory.getObject();
                        engineTrafficeResultVo.setGb("nodefactor");
                        engineTrafficeResultVo.setNodeFactorListVo(nodeFactorListVo);

                        engineSendPrdAmqp.sendMessageCmd(engineTrafficeResultVo);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [NiaSdnNodeFactorHdlService] niaSdnNodeFactorHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
