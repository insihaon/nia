package com.nia.ai.traffic.preprocessor.service.impl;

import com.nia.ai.traffic.preprocessor.amqp.EngineSendPrdAmqp;
import com.nia.ai.traffic.preprocessor.mapper.AttAiTcaMapper;
import com.nia.ai.traffic.preprocessor.mapper.CommonDataMapper;
import com.nia.ai.traffic.preprocessor.service.AttAiTcaService;
import com.nia.ai.traffic.preprocessor.vo.anomalous.AttAiTcaTrafficCheckModelVo;
import com.nia.ai.traffic.preprocessor.vo.anomalous.OverTrafficInfoVo;
import com.nia.ai.traffic.preprocessor.vo.anomalous.TrafficListVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Service("AttAiTcaService")
public class AttAiTcaServiceImpl implements AttAiTcaService {
    private final static Logger LOGGER = Logger.getLogger(AttAiTcaService.class);

    @Autowired
    private EngineSendPrdAmqp engineSendPrdAmqp;

    @Autowired
    private AttAiTcaMapper attAiTcaMapper;

    @Autowired
    private CommonDataMapper commonMapper;

    public void attAiTcaTrafficHdlProcessor(){
        LOGGER.info("=====> [AttAiTcaService] attAiTcaTrafficHdlProcessor start");

        ArrayList<AttAiTcaTrafficCheckModelVo> attAiTcaModelVoList = attAiTcaMapper.selectAttAiTcaTrafficCheckModel();
        String lastDate = commonMapper.selectLinkageYdKey("tca_alert_last_check_time");
        LOGGER.info("lastDate = " + lastDate);

        try{
            if(attAiTcaModelVoList != null && attAiTcaModelVoList.size() > 0){
                for (AttAiTcaTrafficCheckModelVo vo : attAiTcaModelVoList) {
                    vo.setLastCheckTime(lastDate);
                    OverTrafficInfoVo overTrafficInfoVo= attAiTcaMapper.findAttAiOverTraffic(vo);

                    if(overTrafficInfoVo != null){
                        LOGGER.info("=====> [AttAiTcaService] send rabbitMq");

                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        LocalDateTime localDateTime =
                                LocalDateTime.parse(overTrafficInfoVo.getMeasuredDatetime(), formatter);

                        Timestamp timestamp = Timestamp.valueOf(localDateTime);
                        TrafficListVo trafficListVo = new TrafficListVo();
                        trafficListVo.setStrresid(vo.getNodeNum());
                        trafficListVo.setStrresname(vo.getNodeNum());
                        trafficListVo.setNodeId(vo.getNodeId());
                        trafficListVo.setStrifid(vo.getIpsdnIfId());
                        trafficListVo.setStrifnm(vo.getIfId());
                        trafficListVo.setInttimestamp(timestamp);
                        trafficListVo.setTicketRcaResultCode("이상트래픽 TCA 경보");

                        HashMap hashMap1 = new HashMap();
                        hashMap1.put("nodeNum", vo.getNodeNum());
                        hashMap1.put("ifNum", vo.getIfNum());
                        hashMap1.put("measuredDatetime", localDateTime);
                        hashMap1.put("direction", overTrafficInfoVo.getDirection());
                        hashMap1.put("modelId", vo.getModelId());

                        attAiTcaMapper.insertTbAttAiTcaModel(hashMap1);
                        LOGGER.info("=====> [AttAiTcaService] insertTbAttAiTcaModel : " + hashMap1);

                        HashMap hashMap2 = new HashMap();
                        hashMap2.put("gb", "Traffic_AI_TCA");

                        HashMap dataHashMap = new HashMap();
                        ArrayList<TrafficListVo> arrayListTrafficListVo = new ArrayList<TrafficListVo>();
                        arrayListTrafficListVo.add(trafficListVo);
                        dataHashMap.put("data", arrayListTrafficListVo);
                        hashMap2.put("trafficListVo", dataHashMap);
                        engineSendPrdAmqp.sendMessageCmdAttAiTca(hashMap2);

                        LOGGER.info("=====> [AttAiTcaService] send rabbitMq hashMap : " + hashMap2);
                    }
                }
            }

            HashMap hashMap = new HashMap();
            hashMap.put("key", "tca_alert_last_check_time");
            commonMapper.updateLinkageYdKey(hashMap);
        }catch (Exception e) {
            LOGGER.error("=====> [AttAiTcaService] attAiTcaTrafficHdlProcessor error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
