package com.nia.korenrca.server.http.handler.RestfulApi;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import com.nia.korenrca.server.http.handler.RequestHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TsdnReserveHandler extends RequestHandler {
    private static Logger logger = LogManager.getLogger();
    public static final String Name = "/tsdn/selfconfig/reserve";

    public static Router create(Router router) {
        router.post(TsdnReserveHandler.Name).handler(new TsdnReserveHandler());
        return router;
    }

    @Override
    protected Object handleJson(HttpServerRequest serverRequest, JsonObject json) {
        Object result = null;
        try {
            EventBusBridge eventBusBridge = EventBusBridge.getServer();
            if(eventBusBridge != null){
                json.put("event_type", "TSDN_RESERVE_RESULT");
                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET, json);
//                logger.debug(String.format("       [T-SDN Handler] ==============> [CLIENT] SOCKET PUSH [%s] : EventType=%s, TICKET_ID=%s", new Object[]{strSequence, strEventType, received.getTicketId()}));
            }
            result = new JsonObject().put("RESULT", "success");
        } catch (Exception e) {
            logger.error(e);
            result = createErrorMessage(e.getMessage());
        } finally {

        }

        return result;
    }
}

