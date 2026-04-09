// package com.codej.ws.client;

// import java.io.UnsupportedEncodingException;
// import java.lang.reflect.Type;
// import java.util.Collections;
// import java.util.List;
// import java.util.concurrent.ExecutionException;

// import org.json.JSONObject;
// import org.springframework.messaging.simp.stomp.StompFrameHandler;
// import org.springframework.messaging.simp.stomp.StompHeaders;
// import org.springframework.messaging.simp.stomp.StompSession;
// import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
// import org.springframework.stereotype.Service;
// import org.springframework.util.concurrent.ListenableFuture;
// import org.springframework.web.socket.WebSocketHttpHeaders;
// import org.springframework.web.socket.client.standard.StandardWebSocketClient;
// import org.springframework.web.socket.messaging.WebSocketStompClient;
// import org.springframework.web.socket.sockjs.client.SockJsClient;
// import org.springframework.web.socket.sockjs.client.Transport;
// import org.springframework.web.socket.sockjs.client.WebSocketTransport;
// import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

// import com.codej.base.utils.JsonUtil;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// // https://github.com/nickebbutt/stomp-websockets-java-client/blob/master/stomp-java-client/src/main/java/com/od/helloclient/HelloClient.java

// @Slf4j
// @RequiredArgsConstructor
// @Service
// public class WebSocketClient {
//     private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders()
//     // {{
//     //     add("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaW1tYnkiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiYWRkcmVzcyI6IjEyNy4wLjAuMSIsImlhdCI6MTY2Nzc4MjM2OCwiZXhwIjoxNjY3ODY4NzY4fQ._Q_hqBRmxBpqgZzWqhvPWVZqdDsmSvGiT0ujvc1atfQ");
//     // }}
//     ;
    
//     public ListenableFuture<StompSession> connect(String url) {
//         Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
//         List<Transport> transports = Collections.singletonList(webSocketTransport);

//         SockJsClient sockJsClient = new SockJsClient(transports);
//         sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        
//         WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

//         // String url = "ws://127.0.0.1:8009/ws-stomp";
//         // log.info("connect: url={}", url);
//         return stompClient.connect(url, headers, new MyHandler());
//     }
    
//     public void subscribe(StompSession stompSession) throws ExecutionException, InterruptedException {
//         stompSession.subscribe("/sub/HEARTBEAT", new StompFrameHandler() {

//             public Type getPayloadType(StompHeaders stompHeaders) {
//                 return byte[].class;
//             }

//             @Override
//             public void handleFrame(StompHeaders headers, Object payload) {
//                 //EventSource's response has a MIME type ("text/html") that is not "text/event-stream". Aborting the connection.
//                 headers.add("Content-Type","text/event-stream");
//                 headers.add("Cache-Control","no-cache");
//                 String string = new String((byte[]) payload);
//                 JSONObject obj= JsonUtil.parse2(string);
//                 // log.info("received: {}", obj.getString("message"));
//             }
//         });
//     }

//     public void sendHello(StompSession stompSession) throws UnsupportedEncodingException {
//         String jsonHello = "{type:\"BROADCAST\",channelName:\"MY_CHANNEL\",message:\"HELLO\"}";
//         log.info("sendHello: {}", jsonHello);
//         stompSession.send("/pub/emit", jsonHello.getBytes("UTF-8"));
//     }

//     private class MyHandler extends StompSessionHandlerAdapter {
//         @Override
//         public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
//             // log.info("connected");
//         }
//      }

    
//     public static void main(String[] args) throws Exception {
//         WebSocketClient helloClient = new WebSocketClient();

//         String url = "ws://127.0.0.1:8009/ws-stomp";
//         ListenableFuture<StompSession> f = helloClient.connect(url);
//         StompSession stompSession = f.get();

//         helloClient.subscribe(stompSession);

//         log.info("Sending hello message" + stompSession);
//         helloClient.sendHello(stompSession);
//         Thread.sleep(60000);
//     }
    
// }