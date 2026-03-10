// package com.codej.ws.client;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
// import org.springframework.boot.context.event.ApplicationReadyEvent;
// import org.springframework.context.event.EventListener;
// import org.springframework.messaging.simp.stomp.StompSession;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;
// import org.springframework.util.concurrent.ListenableFuture;

// import com.codej.base.domain.common.dto.AppDto;

// import lombok.extern.slf4j.Slf4j;

// // https://github.com/nickebbutt/stomp-websockets-java-client/blob/master/stomp-java-client/src/main/java/com/od/helloclient/HelloClient.java

// @Slf4j
// @ConditionalOnExpression("'${myconf.websocket.test.enabled:false}' == 'true'")
// @Service
// public class WebSocketClientLuncher {
//    @Autowired
//    AppDto appdto;

//    @Value("${myconf.websocket.test.max:3000}")
//    private int max;

//    private int count = 0;
//    @EventListener(ApplicationReadyEvent.class)
//    public void onAfterStartup() throws Exception {
//       createNewConnct();
//    }

//    private void createNewConnct() {
//       try {
//          WebSocketClient helloClient = new WebSocketClient();
//          String url = appdto.getWsUrl().startsWith("//") ? ("ws:" + appdto.getWsUrl()) : String.format("ws://127.0.0.1:%s%s", appdto.getPort(), appdto.getWsUrl());
//          ListenableFuture<StompSession> f = helloClient.connect(url);
//          StompSession stompSession = f.get();
//          helloClient.subscribe(stompSession);
//          log.info("created WebSocketClient: count={}, url={}", ++count, url);
//       } catch (Exception e) {
//          e.printStackTrace();
//       }
//    }

//    @Scheduled(cron = "${myconf.websocket.test.cron:*/10 * * * * *}")
//    public void cronCreateNewConnct() {
//       if(count < max) 
//          createNewConnct();
//    }
// }