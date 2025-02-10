package com.codej.nia.mq.handler;

import java.nio.charset.StandardCharsets;

import com.codej.nia.properties.NiaRabbitMQProperites;
import com.rabbitmq.client.Channel;
import com.codej.base.dto.Channel.EmChannel;
import com.codej.base.dto.SocketMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.codej.ws.service.WebsocketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmqpNiaAlarmListener implements ChannelAwareMessageListener {

    @Autowired(required = false)
    @Lazy
    private WebsocketService websocketService;

    @Autowired
    private NiaRabbitMQProperites niaRabbitMQProperites;

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            log.info("[MQ:{}] >>> onMessage : {}", niaRabbitMQProperites.getNiaEngineToUi(),
                    body.substring(0, Math.min(200, body.length())));

            /*
             * "{"result":"success","properties":{"sop_id":"95597","status":"FIN"},"ticketId":"1620541","eventType":"TICKET_UPDATE","ticketType":null}"
             */

            SocketMessage socketMessage = SocketMessage.builder()
                    .type(SocketMessage.EmMessage.BROADCAST)
                    .channelName(EmChannel.IPSDN_ALARM)
                    .sender("SYSTEM")
                    .message(body)
                    .build();
            websocketService.sendMessage(socketMessage);

            log.info("websocket sendIpsdnAlarm Message: {}", socketMessage);
        } catch (Exception e) {
            log.error("=====> [{}] onMessage error {} <=====", this.getClass().getSimpleName(),
                    ExceptionUtils.getStackTrace(e));
        }
    }
}
