package com.nia.ai.traffic.preprocessor.listener;

import com.nia.ai.traffic.preprocessor.common.UtlCommon;
import com.nia.ai.traffic.preprocessor.mapper.TrafficMapper;
import com.nia.ai.traffic.preprocessor.service.NiaNoxiousTrafficHdlService;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficListVo;
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
public class AiNoxiousTrafficMsgListener implements ChannelAwareMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AiNoxiousTrafficMsgListener.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<NoxiousTrafficListVo> noxiousTrafficListVoObjectFactory;

    @Autowired
    @Qualifier("NiaNoxiousTrafficHdlService")
    private NiaNoxiousTrafficHdlService niaNoxiousTrafficHdlService;

    @Autowired
    private NoxiousTrafficListVo noxiousTrafficListVo;

    @Autowired
    private TrafficMapper trafficMapper;

    @Override
    public void onMessage (Message message, Channel channel) {

        try {
            Object obj;
            HashMap<String, String> strHashMap;
            String msg = new String(message.getBody());


            noxiousTrafficListVo = noxiousTrafficListVoObjectFactory.getObject();

            obj = UtlCommon.jsonToObject(noxiousTrafficListVo, msg);
            noxiousTrafficListVo = (NoxiousTrafficListVo) obj;

            LOGGER.info(">>>>>>>>>>[AiNoxiousTrafficeMsgListener] onMessage : " + noxiousTrafficListVo.getData().size() + " <<<<<<<<<<<<<<<<<");


            niaNoxiousTrafficHdlService.niaNoxiousTrafficeHdlProcessor(noxiousTrafficListVo);

            strHashMap = new HashMap<>();
            strHashMap.put("key", "aiTrafficNoxKey");
            trafficMapper.updateLinkageYdKey(strHashMap);

        } catch (Exception e) {
            LOGGER.error("=====> [AiNoxiousTrafficeMsgListener] onMessage error " + ExceptionUtils.getStackTrace(e) + "<=====");
        }
    }
}
