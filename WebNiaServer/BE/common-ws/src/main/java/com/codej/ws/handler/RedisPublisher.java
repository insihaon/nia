package com.codej.ws.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.codej.base.dto.SocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisPublisher {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired(required = false)
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis 에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     * RedisSubscriber 를 구현해야 한다. [참고] https://bit.ly/3GfjwRS
     * 현재는 messagingTemplate 를 통해서 웹소캣에 직접 convertAndSend 를 한다
     * 사용법
     * redisPublisher.sendMessage("message");
     */
    public void sendMessage(String publishMessage) {
        if(messagingTemplate == null) return;
        try {
            // SocketMessage 객채로 맵핑
            SocketMessage socketMessage = objectMapper.readValue(publishMessage, SocketMessage.class);
            // 채널을 구독한 클라이언트에게 메시지 발송
            
            String destination = String.format("/sub/%s", socketMessage.getChannelName());
            // log.info("websocket send {}: {}", destination, socketMessage);
            messagingTemplate.convertAndSend(destination, socketMessage);
        } catch (Exception e) {
            log.error("sendMessage error: {}", e.getMessage());
        }
    }
}
