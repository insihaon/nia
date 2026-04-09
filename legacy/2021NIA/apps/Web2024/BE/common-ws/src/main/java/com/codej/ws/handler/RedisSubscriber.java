// package com.codej.ws.handler;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.codej.web.ws.dto.SocketMessage;

// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.data.redis.connection.Message;
// import org.springframework.data.redis.connection.MessageListener;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Service;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @RequiredArgsConstructor
// @Service
// @ConditionalOnProperty(name="myconf.websocket.enabled", havingValue="true")
// public class RedisSubscriber implements MessageListener {

//     private final ObjectMapper objectMapper;
//     private final RedisTemplate redisTemplate;

//     @Override
//     public void onMessage(Message message, byte[] pattern) {
//         try {
//             String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//             SocketMessage socketMessage = objectMapper.readValue(body, SocketMessage.class);
//             log.info("Room - Message : {}", socketMessage.toString());
//         } catch (Exception e) {
//             log.error(e.getMessage());
//         }
//     }
// }
