package com.codej.ws.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.codej.base.dto.AppDto;
import com.codej.ws.handler.StompHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
@ConditionalOnProperty(name = "myconf.websocket.enabled", havingValue = "true")
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final AppDto appDto;
    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // - 에러: 디버깅 URL을 localhost 로 접근했을 때 발생하는 CORS 에러
        // - 원인: setAllowedOrigins("*") 가 /ws-stomp/info 형태의 URL 에 적용되지 않는것으로 추측된다.
        // (SpringBoot 를 2.5.5 로 업데이트 후 발생)
        // - 해결: setAllowedOrigins 대신 setAllowedOriginPatterns("*") 를 사용하니 적용이 되었다.
        // * 단, setAllowedOrigins("*") 는 반드시 제거해 줘야 한다.

        String url = appDto.getWsUrl();
        // if(!url.matches("^\\/[^/].*")) {
        log.warn("websocket url '{}' 는 addEndpoint 적용에 문제가 발생 할 수 있습니다.", url);
        // }

        registry.addEndpoint(url)
                // .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작 할 수 있게 합니다.
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}