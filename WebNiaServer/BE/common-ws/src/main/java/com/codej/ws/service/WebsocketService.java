package com.codej.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.SocketMessage;

@Service
@ConditionalOnProperty(name="myconf.websocket.enabled", havingValue="true")
public class WebsocketService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChannelService channelService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private AppDto appDto;

    public WebsocketService(@Lazy ChannelTopic channelTopic, @Lazy RedisTemplate redisTemplate, @Lazy ChannelService channelService, @Lazy SimpMessageSendingOperations messagingTemplate) {
        
        // 순환 참조에 대한 해법 : (@Autowired) > (생성자 + @Lazy)
        // ┌─────┐
        // |  webSocketConfiguration defined in file [D:\Work\RMS2022_kepco\workspace\backend\common-web\bin\main\com\codej\core\config\WebSocketConfiguration.class]
        // ↑     ↓
        // |  stompHandler defined in file [D:\Work\RMS2022_kepco\workspace\backend\common-web\bin\main\com\codej\core\ws\handler\StompHandler.class]
        // ↑     ↓
        // |  websocketService (field private org.springframework.messaging.simp.SimpMessageSendingOperations com.codej.web.ws.service.WebsocketService.messagingTemplate)
        // ↑     ↓
        // |  org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration
        // └─────┘

        this.channelTopic = channelTopic;
        this.redisTemplate = redisTemplate;
        this.channelService = channelService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * destination정보에서 channelName 추출
     */
    public String getChannelName(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채널에 메시지 발송
     */
    public void sendMessage(SocketMessage socketMessage) {
        // socketMessage.setUserCount(channelService.getUserCount(socketMessage.getChannelName()));
        // if (SocketMessage.EmMessage.ENTER.equals(socketMessage.getType())) {
        //     String msg = String.format("%s 님이 %s 채널에 입장했습니다.", socketMessage.getSender(), socketMessage.getChannelName());
        //     socketMessage.setMessage(msg);
        //     socketMessage.setSender("[알림]");
        // } else if (SocketMessage.EmMessage.QUIT.equals(socketMessage.getType())) {
        //     String msg = String.format("%s 님이 %s 채널을 나갔습니다.", socketMessage.getSender(), socketMessage.getChannelName());
        //     socketMessage.setMessage(msg);
        //     socketMessage.setSender("[알림]");
        // }

        Boolean enabled = false;
        if (enabled && appDto.getRedisEnabled()) {
            // 2022.09.02 현재는 redisTemplate 을 사용해도 웹 소켓으로 직접 전달한다
            // RedisPublisher.java sendMessage 참고
            redisTemplate.convertAndSend(channelTopic.getTopic(), socketMessage);
        } else {
            String destination = String.format("/sub/%s", socketMessage.getChannelName());
            messagingTemplate.convertAndSend(destination, socketMessage);
        }
    }
}
