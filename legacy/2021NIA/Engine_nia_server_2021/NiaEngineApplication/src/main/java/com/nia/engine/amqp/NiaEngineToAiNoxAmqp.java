package com.nia.engine.amqp;

import com.nia.engine.listener.NiaAiAnoToEngineMsgListener;
import com.nia.engine.listener.NiaAiNoxToEngineMsgListener;
import com.nia.engine.mapper.TicketMapper;
import com.nia.engine.vo.RcaEngineResult;
import com.nia.engine.vo.engineToAi.EngineToAiNoxVo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class NiaEngineToAiNoxAmqp {

    private static final Logger LOGGER = LoggerFactory.getLogger(NiaEngineToAiNoxAmqp.class);

    @Autowired
    @Qualifier("NiaEngineToAiNox_RabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NiaAiNoxToEngineMsgListener msgListener;

    @Autowired
    private TicketMapper ticketMapper;

    public void sendMessageCmd(EngineToAiNoxVo rcaEngineResult) {
        try{
            HashMap<String, String> map = new HashMap<>();
            LOGGER.info("=====> [sendMessageCmd] : rcaEngineResult " +rcaEngineResult.toString() + " <=====");
            rabbitTemplate.convertAndSend(rcaEngineResult);

            map.put("ticketId", String.valueOf(rcaEngineResult.getTicketId()));
            LOGGER.info("=====> [sendMessageCmd] insert SendTime Sop :" + rcaEngineResult.getTicketId());
            ticketMapper.insertSendTimeTicket(map);

            //Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
            try {
                Thread.sleep(1000);
                rabbitTemplate.convertAndSend(rcaEngineResult);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
