package com.nia.korenrca.server.worker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import com.nia.korenrca.server.service.RCAService;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.properties.rca.SyslogProperties;

import io.vertx.core.json.JsonObject;

public class SyslogManager extends Thread {
    private static Logger LOGGER = LogManager.getLogger();

    private Timestamp maxSequence = null;
    RCAService service = new RCAService();

    public void run() {
        try {
            updateMaxSequence(service);
            service.close();

            while (true) {
                try {
                    Data param = new Data();
                    param.set(SyslogProperties.ALARM_TIME, maxSequence);
                    ArrayList<HashMap<String, Object>> result = service.selectList(Constants.NIA.SQL.SELECT_NIA_SYSLOG_LIST, param);

                    broadcast(result);
                    updateMaxSequence(service);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    service.close();
                    Thread.sleep(10000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void updateMaxSequence(RCAService service) throws Exception {
       Data resultMax = service.selectOneData(Constants.NIA.SQL.SELECT_LAST_SYSLOG_TIME, new Data().set("", null));
       Timestamp max = (Timestamp) resultMax.get("max");
        if (maxSequence == null || maxSequence.getTime() < max.getTime() ) {
            maxSequence = max;
            LOGGER.log(Level.DEBUG, "LAST SYSLOG UPDATED : " + maxSequence);
        }
    }

    private void broadcast(ArrayList<HashMap<String, Object>> result) {
        if (result.size() <= 0) return;
        
        String logText = " ====== SOCKET PUSH ======>        SYSLOG: No." + result.get(0).get("alarmno");
        if (result.size() > 2) {
            logText = logText + " 외 " + (result.size() - 1) + "개";
        }
        try {
            EventBusBridge eventBusBridge = EventBusBridge.getServer();
            if(eventBusBridge != null){
                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_SYSLOG, new JsonObject().put("data", result));
            }
            LOGGER.log(Level.DEBUG, logText);
        } catch (NullPointerException | IllegalStateException e) {
            EventBusBridge.connectionFailed();
        }
    }
}
