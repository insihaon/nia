package com.codej.nia.mq.handler;

import java.nio.charset.StandardCharsets;

import com.codej.nia.properties.NiaRabbitMQProperites;
import com.rabbitmq.client.Channel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmqpNiaAlarmListener implements ChannelAwareMessageListener {

    // @Autowired
    // private WebsocketService websocketService;

    @Autowired
    private NiaRabbitMQProperites niaRabbitMQProperites;


    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            log.info("[MQ:{}] >>> onMessage : {}", niaRabbitMQProperites.getNiaEngineToUi(), body.substring(0, Math.min(200, body.length())));

            // #region case1. alarm 을 처리하는 경우
            // amqpAlarm = JsonUtil.convertJsonToObject(msg, amqpAlarm);
            // alarmService.handleReceivedAlarm(amqpAlarm);
            // #endregion

            // log.info("[MQ:{}] >>> onMessage : CheckPoint 1",
            // rabbitMQProperites.getTopasAlarmQueue());

            // #region case2. 그대로 websocket 로 전달하는 경우 (프록시 역활)
            // @formatter:off
            // SocketMessage socketMessage = SocketMessage.builder()
            //         .type(SocketMessage.EmMessage.BROADCAST)
            //         .channelName(com.codej.base.dto.Channel.EmChannel.NIA_ALARM)
            //         .sender(niaRabbitMQProperites.getNiaEngineToUi())
            //         .message(body)
            //         .build();
            // websocketService.sendMessage(socketMessage);
            // @formatter:on
            // #endregion

            // log.info("[MQ:{}] >>> onMessage : CheckPoint 2",
            // rabbitMQProperites.getTopasAlarmQueue());

        } catch (Exception e) {
            log.error("=====> [{}] onMessage error {} <=====", this.getClass().getSimpleName(),
                    ExceptionUtils.getStackTrace(e));
        }
    }
}
