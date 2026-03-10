package com.codej.demo.mq.handler;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codej.demo.mq.properties.DemoRabbitMQProperites;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmqpAlarmListener implements ChannelAwareMessageListener {

    // @Autowired
    // private WebsocketService websocketService;

    @Autowired
    private DemoRabbitMQProperites rabbitMQProperites;


    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);

            log.info("[MQ:{}] >>> onMessage : {}", rabbitMQProperites.getEngineToUi(), body.substring(0, Math.min(200, body.length())));
          
        } catch (Exception e) {
            log.error("=====> [{}] onMessage error {} <=====", this.getClass().getSimpleName(),
                    ExceptionUtils.getStackTrace(e));
        }
    }
}
