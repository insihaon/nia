package com.codej.base.dto;

import java.util.Optional;

import org.springframework.util.Assert;

import com.codej.base.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocketMessage {

    public SocketMessage() {
        this.sequence = DateUtil.getTimestamp();
    }

    @Builder
    public SocketMessage(EmMessage type, Channel.EmChannel channelName, String sender, String message, long userCount, long sessionCount) {
        this();
        this.type = Optional.ofNullable(type).orElse(EmMessage.BROADCAST).toString();
        this.channelName = Optional.ofNullable(channelName).orElse(Channel.EmChannel.UNKNOWN).toString();
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
        this.sessionCount = sessionCount;

        Assert.notNull(this.type, "type must not be null");
        Assert.notNull(this.channelName, "channelName must not be null");
    }

    // 메시지 타입 : 입장, 퇴장, 알림
    public enum EmMessage {
        ENTER, QUIT, BROADCAST
    }

    protected long sequence;
    protected String type; // 메시지 타입
    protected String channelName = Channel.EmChannel.UNKNOWN.toString(); // 채널ID
    protected String sender; // 메시지 보낸사람
    protected String message; // 메시지
    protected long userCount; // 채널 구독자수, 채널 내에서 메시지가 전달 될 때 구독자수 갱신 시 사용
    protected long sessionCount; 
}
