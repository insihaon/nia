package com.codej.ws.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codej.base.dto.Channel;
import com.codej.base.dto.SocketMessage;
import com.codej.base.utils.DateUtil;
import com.codej.ws.service.ChannelService;
import com.codej.ws.service.WebsocketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnExpression("'${myconf.websocket.enabled}' == 'true' && '${myconf.websocket.heartbeat.enabled}' == 'true'")
public class HeartbeatTask {

    @Autowired(required = false)
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ChannelService channelRepository;
    
    @Autowired(required = false)
    private WebsocketService websocketService;
    
    //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    // 매 10초마다 heartbeat 을 보냄
    @Scheduled(cron = "*/10 * * * * *") 
    public void sendHeartbeat() {
        try {
            String now = DateUtil.getCurrentTime(DateUtil.YYYYMMDDHHMMSS_NORMAL);
            // log.info("sendHeartbeat {}", now);

            // case 1. use domain
            // @formatter:off
            SocketMessage message = SocketMessage.builder()
                .type(SocketMessage.EmMessage.BROADCAST)
                .channelName(Channel.EmChannel.HEARTBEAT)
                .sender("SYSTEM")
                .message(now)
                .userCount(channelRepository.getUserCount())
                .sessionCount(channelRepository.getSessionCount())
                .build();
            // @formatter:on
            
            // case 2. use hashmap
            // Data data = new Data();
            // data.set("type", "NOTICE");
            // data.set("sender", "SYSTEM");
            // data.set("message", now);
            // Map<?, ?> message = data.getMap();

            // websocketService 를 사용하면 redisTemplate.convertAndSend Redis를 통해서 전달해야 하는데 
            // 2022.09.02 현재는 redisTemplate 을 사용해도 messagingTemplate.convertAndSend 를 통해서 전달된다. 즉, Redis를 사용하지 않는다.
            // todo: RedisSubscriber.java 구현과 RedisPublisher.java 수정을 통하면 Redis를 사용하도록 변경한다.
            websocketService.sendMessage(message);

            // HEARTBEAT 은 Redis를 통한 공유가 필요 없으므로 직접 메시지를 전송한다
            // String destination = "/sub/HEARTBEAT";
            // log.info("websocket send {}: {}", destination, message);
            // messagingTemplate.convertAndSend("/sub/HEARTBEAT", message);
        } catch (Exception e) {
            log.error("sendHeartbeat error: {}", e.getMessage());
        }
    }
   
}
