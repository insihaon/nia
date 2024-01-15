package com.codej.base.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class Channel implements Serializable {

    public enum EmChannel {
        UNKNOWN, HEARTBEAT, RELOAD, 
        AAM_ALARM
    }

	private static final long serialVersionUID = 6494678977089006639L;

    private String uuid;
    private String name;
    private long userCount; // 채널 구독자수

    public static Channel create(String name) {
        Channel channel = new Channel();
        channel.uuid = UUID.randomUUID().toString();
        channel.name = name;
        return channel;
    }
    
}

