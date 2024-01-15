package com.codej.mq.handler;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import com.codej.base.dto.Channel.EmChannel;
import com.codej.base.dto.SocketMessage;
import com.codej.base.eventbus.WsEventPublisher;
import com.codej.mq.properties.RabbitMQProperites;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnExpression("'${spring.rabbitmq.enabled:true}' == 'true'")
public class AamAlarmListener implements ChannelAwareMessageListener {
    @Autowired(required = false)
    private WsEventPublisher wsEventPublisher;

    @Autowired(required = false)
    private RabbitMQProperites rabbitMQProperites;

    public static String substringLeft(String s, int len) {
        return s.substring(0, Math.min(len, s.length()));
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        try {

            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            log.info("[MQ:{}] >>> onMessage : {}", rabbitMQProperites.getAamAlarmQueue(),
                    substringLeft(body, 200) + "...");

            // #region case1. alarm 을 처리하는 경우
            // amqpAlarm = JsonUtil.convertJsonToObject(msg, amqpAlarm);
            // alarmService.handleReceivedAlarm(amqpAlarm);
            // #endregion

            // #region case2. 그대로 websocket 로 전달하는 경우 (프록시 역활)
            // @formatter:off
            SocketMessage socketMessage = SocketMessage.builder()
                    .type(SocketMessage.EmMessage.BROADCAST)
                    .channelName(EmChannel.AAM_ALARM)
                    .sender(rabbitMQProperites.getAamAlarmQueue())
                    .message(body)
                    .build();
            wsEventPublisher.publishEvent(socketMessage);
            // @formatter:on
            // #endregion

        } catch (Exception e) {
            log.error("=====> [{}] onMessage error {} <=====", this.getClass().getSimpleName(),
                    ExceptionUtils.getStackTrace(e));
        }
    }
}
