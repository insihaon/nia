package com.codej.ws.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.SocketMessage;
import com.codej.base.provider.BaseJwtTokenProvider;
import com.codej.ws.service.ChannelService;
import com.codej.ws.service.WebsocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@ConditionalOnProperty(name="myconf.websocket.enabled", havingValue="true")
public class WebSocketController extends BaseController {

    private final BaseJwtTokenProvider baseJwtTokenProvider;
    private final ChannelService channelService;
    private final WebsocketService websocketService;

    /**
     * websocket "/pub/emit"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/emit")
    public void emit(SocketMessage message, @Header("token") String token) {
        String userId;
        try {
            userId = baseJwtTokenProvider.getUserNameFromJwt(token);
            log.info("MessageMapping /pub/emit: userId={}, message={}", userId, message.toString());
            // 로그인 회원 정보로 대화명 설정
            message.setSender(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 채널 구독자수 세팅
        String channelName = message.getChannelName();
        message.setUserCount(channelService.getUserCount(channelName));
        // Websocket에 발행된 메시지를 redis로 발행(publish) > RedisPublisher.sendMessage 
        websocketService.sendMessage(message);
    }
}
