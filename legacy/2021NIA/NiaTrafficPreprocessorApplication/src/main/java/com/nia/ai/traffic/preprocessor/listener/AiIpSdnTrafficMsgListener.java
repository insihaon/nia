package com.nia.ai.traffic.preprocessor.listener;

import com.nia.ai.traffic.preprocessor.common.UtlCommon;
import com.nia.ai.traffic.preprocessor.mapper.TrafficMapper;
import com.nia.ai.traffic.preprocessor.service.NiaSdnTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.sdn.traffic.SdnTrafficJsonVo;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AiIpSdnTrafficMsgListener implements ChannelAwareMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AiIpSdnTrafficMsgListener.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SdnTrafficJsonVo> sdnTrafficJsonVoObjectFactory;

    @Autowired
    @Qualifier("NiaSdnTrafficHdlService")
    private NiaSdnTrafficHdlService niaSdnTrafficHdlService;

    @Autowired
    private SdnTrafficJsonVo sdnTrafficJsonVo;

    @Autowired
    private TrafficMapper trafficMapper;

    @Override
    public void onMessage (Message message, Channel channel) {

        try {
            Object obj;
            HashMap<String, String> strHashMap;
            String msg = new String(message.getBody());

            sdnTrafficJsonVo = sdnTrafficJsonVoObjectFactory.getObject();

            obj = UtlCommon.jsonToObject(sdnTrafficJsonVo, msg);
            sdnTrafficJsonVo = (SdnTrafficJsonVo) obj;

            LOGGER.info(">>>>>>>>>>[AiSdnTrafficeMsgListener] onMessage : " + sdnTrafficJsonVo.getData().getData().size() + " <<<<<<<<<<<<<<<<<");
			LOGGER.info(">>>>>>>>>>[AiSdnTrafficeMsgListener] onMessage : " + sdnTrafficJsonVo.getData().getData() + " <<<<<<<<<<<<<<<<<");

            niaSdnTrafficHdlService.niaSdnTrafficeHdlProcessor(sdnTrafficJsonVo.getData());

            strHashMap = new HashMap<>();
            strHashMap.put("key", "aiTrafficAnoKey");
            trafficMapper.updateLinkageYdKey(strHashMap);


        } catch (Exception e) {
            LOGGER.error("=====> [AiSdnTrafficeMsgListener] onMessage error " + ExceptionUtils.getStackTrace(e) + "<=====");
        }
    }
}
