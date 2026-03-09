package com.nia.korenrca.server.socket.handler;

import com.nia.korenrca.constants.Constants;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import org.apache.logging.log4j.LogManager;

/**
 * Created by Mobidigm on 2018-08-14.
 */
public class BridgeEventHandler implements Handler<BridgeEvent> {

    private static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final EventBus eventBus;
    private final SocketRepository repository;
    public int count;

    public BridgeEventHandler(EventBus eventBus, SocketRepository repository) {
        this.eventBus = eventBus;
        this.repository = repository;
        this.count = 0;
    }

    @Override public void handle(BridgeEvent event) {
        if (event.type() == BridgeEventType.SOCKET_CREATED) {   //소켓생성
            if (event.socket().uri().equals(Constants.EVENT.WebsocketUri)) {
//                LOGGER.info("웹서버 -> 소켓서버 접속 성공!");
                event.complete(true);
                return;
            }
            String text1 = event.socket().hashCode() + "님 socket 접속";
            String text2 = " 현재 서버 접속자 수는 " + (++this.count) + "명 입니다.";
            LOGGER.info(text1 + text2);

            JsonObject json = new JsonObject();
            json.put("CURRENT_USER_COUNT", this.count);
            this.eventBus.publish(Constants.EVENT.ADDR_OUT_SESSION, json);
        }

        if (event.type() == BridgeEventType.SOCKET_CLOSED) {    //소켓소멸
            if (event.socket().uri().equals(Constants.EVENT.WebsocketUri)) {
                LOGGER.error("웹서버 -> 소켓서버 연결 끊김");
                event.complete(true);
                return;
            }
            String text1 = event.socket().hashCode() + "님 socket 퇴장";
            String text2 = " 현재 서버 접속자 수는 " + (--this.count) + "명 입니다.";
            LOGGER.info(text1 + text2);

            JsonObject json = new JsonObject();
            json.put("CURRENT_USER_COUNT", this.count);
            this.eventBus.publish(Constants.EVENT.ADDR_OUT_SESSION, json);
        }

        if (event.type() == BridgeEventType.SEND) {
            // SEND ... nothing
        } else if (event.type() == BridgeEventType.PUBLISH) {    //PUBLISH
            if (event.getRawMessage().getString("address").equals(Constants.EVENT.ADDR_IN_DEFAULT)) {
                this.eventBus.publish(Constants.EVENT.ADDR_OUT_DEFAULT, event.getRawMessage().getJsonObject("body"));
            }
            if (event.getRawMessage().getString("address").equals(Constants.EVENT.ADDR_IN_SESSION)) {
                JsonObject json = new JsonObject();
                json.put("CURRENT_USER_COUNT", this.count);
                this.eventBus.publish(Constants.EVENT.ADDR_OUT_SESSION, json);
            }
        }
        event.complete(true);
    }
}
