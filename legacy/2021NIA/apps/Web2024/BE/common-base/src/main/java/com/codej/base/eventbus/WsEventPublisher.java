package com.codej.base.eventbus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.codej.base.dto.SocketMessage;

@Component
public class WsEventPublisher {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void publishEvent(SocketMessage socketMessage) {
        eventPublisher.publishEvent(new WsEvent(this, socketMessage));
    }
}