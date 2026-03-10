package com.codej.base.eventbus;

import org.springframework.context.ApplicationEvent;

import com.codej.base.dto.SocketMessage;

public class WsEvent extends ApplicationEvent {
    private SocketMessage message;
    
    public WsEvent(Object source, SocketMessage socketMessage) {
        super(source);
        this.message = message;
    }

    public SocketMessage getMessage() {
        return this.message;
    }

}
