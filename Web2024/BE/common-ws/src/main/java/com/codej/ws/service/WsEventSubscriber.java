package com.codej.ws.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.codej.base.eventbus.BaseWsEventSubscriber;
import com.codej.base.eventbus.WsEvent;

public class WsEventSubscriber extends BaseWsEventSubscriber {

    @Autowired
    private WebsocketService websocketService;

    @Override
    public void onApplicationEvent(WsEvent event) {
        websocketService.sendMessage(event.getMessage());
    }
   
}
