package com.codej.ws.handler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.Channel;
import com.codej.base.dto.DbUser;
import com.codej.base.provider.BaseJwtTokenProvider;
import com.codej.ws.dto.SessionUser;
import com.codej.ws.service.ChannelService;
import com.codej.ws.service.WebsocketService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "myconf.websocket.enabled", havingValue = "true")
public class StompHandler implements ChannelInterceptor {

    private final BaseJwtTokenProvider baseJwtTokenProvider;
    private final ChannelService channelRepository;
    private final WebsocketService websocketService;
    private final AppDto appDto;

    // websocket을 통해 들어온 요청이 처리 되기 전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

            // if (StompCommand.SEND != accessor.getCommand()) {
            // ResourceUtil.printMemory2(CommonUtil.getStackTrace(
            // String.format("$METHOD($FILE:$LINE) COMMAND:%s",
            // accessor.getCommand().toString())));
            // }

            if (StompCommand.CONNECT == accessor.getCommand()) {
                // #region websocket 연결요청
                String jwtToken = accessor.getFirstNativeHeader("token");
                String sessionId = (String) message.getHeaders().get("simpSessionId");

                // Header의 jwt token 검증
                if (baseJwtTokenProvider.validateToken(jwtToken, "StompHandler")) {
                    SessionUser sessionUser = null;
                    if (appDto.getWsCanAccessUser() == true) {
                        DbUser user = (DbUser) baseJwtTokenProvider.getUserDetails(jwtToken);
                        sessionUser = new SessionUser(user);
                    } else {
                        Claims claims = baseJwtTokenProvider.getClaims(jwtToken);
                        sessionUser = new SessionUser(claims);
                    }

                    String uid = sessionUser != null ? sessionUser.getUid() : "";
                    String address = sessionUser != null ? sessionUser.getAddress() : "";

                    // if(appDto.getAuthSingleOnly()) {
                    // SocketMessage socketMessage = SocketMessage.builder()
                    // .type(SocketMessage.EmMessage.BROADCAST)
                    // .channelName(EmChannel.COMMAND)
                    // .sender(this.getClass().getSimpleName())
                    // .message("LOGOUT")
                    // .build();
                    // websocketService.sendMessage(socketMessage);
                    // }
                    log.info("CONNECT jwtToken={}, sessionId={}, userId={}, address={}", jwtToken, sessionId, uid,
                            address);

                    // 세션 유저에 등록
                    channelRepository.addSession(sessionId, sessionUser);
                    sessionUser = null;
                }

                // #endregion
            } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
                // #region 채널 구독요청
                // header정보에서 구독 destination정보를 얻고, channelName를 추출한다.
                String channelName = websocketService
                        .getChannelName(Optional.ofNullable((String) message.getHeaders().get("simpDestination"))
                                .orElse(Channel.EmChannel.UNKNOWN.name()));

                // 채널에 들어온 클라이언트 sessionId를 channelName와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채널에 들어가 있는지 알기
                // 위함)
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                channelRepository.addSessionChannel(sessionId, channelName);
                SessionUser sessionUser = channelRepository.getSessionUser(sessionId);

                // 채널의 구독자수를 +1한다.
                long userCount = channelRepository.plusUserCount(channelName);

                // 클라이언트 입장 메시지를 채널에 발송한다.(redis publish)
                // String name = Optional.ofNullable((Principal)
                // message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
                String name = (String) message.getHeaders().get("simpUser");

                // 알림메시지를 발송한다.
                // redisService.sendMessage(SocketMessage.builder().type(SocketMessage.EmMessage.ENTER).channelName(channelName).sender(name).build());

                String uid = "";
                String address = "";
                if (sessionUser != null) {
                    uid = sessionUser.getUid();
                    address = sessionUser.getAddress();
                }

                log.info("SUBSCRIBED channelName={}, userId={}, address={}, userCount={}, name={}", channelName, uid,
                        address, userCount, name);
                // #endregion
            } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                String channelName = (String) message.getHeaders().get("simpSubscriptionId");

                if (channelName != null) {
                    // 채널의 구독자수를 -1한다.
                    channelRepository.minusUserCount(channelName);
                    // UNSUBSCRIBE 클라이언트의 channelName 맵핑 정보를 삭제한다.
                    channelRepository.removeSessionChannel(sessionId, channelName);

                    Set<String> channelNames = channelRepository.getSessionChannel(sessionId);
                    if (channelNames != null) {
                        log.info("UNSUBSCRIBE channelName={}, subscribed=[{}]", channelName,
                                String.join(", ", channelNames));
                    }
                }

            } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
                // #region Websocket 연결 종료
                // 연결이 종료된 클라이언트 sesssionId로 채널 id를 얻는다.
                String sessionId = (String) message.getHeaders().get("simpSessionId");

                Set<String> channelNames = channelRepository.getSessionChannel(sessionId);
                SessionUser sessionUser = channelRepository.getSessionUser(sessionId);

                if (channelNames == null) {
                    return message;
                }

                Duration duration = Duration.ZERO;
                if (sessionUser != null) {
                    LocalDateTime connectTime = sessionUser.getConnectTime();
                    duration = Duration.between(connectTime, LocalDateTime.now());
                }

                // 채널의 구독자수를 -1한다.
                for (String channelName : channelNames) {
                    channelRepository.minusUserCount(channelName);
                }

                // 클라이언트 퇴장 메시지를 채널에 발송한다.(redis publish)
                // String name = Optional.ofNullable((Principal)
                // message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
                String name = (String) message.getHeaders().get("simpUser");
                String uid = "";
                String address = "";
                if (sessionUser != null) {
                    uid = sessionUser.getUid();
                    address = sessionUser.getAddress();
                }

                // 알림메시지를 발송한다.
                // redisService.sendMessage(SocketMessage.builder().type(SocketMessage.EmMessage.QUIT).channelName(channelName).sender(name).build());

                // DISCONNECT 클라이언트의 channelName 맵핑 정보를 삭제한다.
                channelRepository.removeSession(sessionId);

                // String strDuration = String.format("days:%d hours:%d minutes:%d seconds:%d",
                // duration.toDays(), duration.toHours(),
                // duration.toMinutes(), duration.toMillis() / 1000);

                log.info("DISCONNECTED sessionId={}, userId={}, channelNames=[{}], name={}, address={}, duration={}",
                        sessionId, uid, String.join(", ", channelNames), name, address, duration.getSeconds());

                // #endregion
            } else if (StompCommand.SEND == accessor.getCommand()) {
                log.info("SEND Message: payload={}", toString(message.getPayload(), "EMPTY"));
            }

            // if (StompCommand.SEND != accessor.getCommand()) {
            // ResourceUtil.printMemory2(CommonUtil.getStackTrace(
            // String.format("$METHOD($FILE:$LINE) COMMAND:%s",
            // accessor.getCommand().toString())));
            // }

        } catch (Exception e) {
            log.error("{}", e.toString());
        }
        return message;
    }

    public static String toString(Object obj, String defaultValue) {
        return Optional.ofNullable(obj.toString()).orElse(defaultValue);
    }
}
