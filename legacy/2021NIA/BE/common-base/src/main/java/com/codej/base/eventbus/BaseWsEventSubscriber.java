package com.codej.base.eventbus;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseWsEventSubscriber implements ApplicationListener<WsEvent> {
    // 추상 메서드로 변경
    public abstract void onApplicationEvent(WsEvent event);
}