package com.nia.korenrca.server.worker;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import com.nia.korenrca.service.util.Utils;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeatbeatManager extends Thread {
    private static Logger LOGGER = LogManager.getLogger();

    public void run() {
        try {

            while (true) {
                try {
                    broadcast(Utils.getCurrentTime());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Thread.sleep(10000);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private void broadcast(String timeString) {
        try {
            JsonObject json = new JsonObject();
            json.put("data", timeString);
            EventBusBridge eventBusBridge = EventBusBridge.getServer();
            if(eventBusBridge != null){
                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_HEATBEAT, json);
            }
//            LOGGER.log(Level.DEBUG, String.format("Broadcast Heatbeat : %s", new Object[]{timeString}));
        } catch (NullPointerException | IllegalStateException e) {
            EventBusBridge.connectionFailed();
        }
    }
}
