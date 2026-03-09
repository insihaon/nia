package com.nia.korenrca.server.worker;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import com.nia.korenrca.server.service.RCAService;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.properties.rca.TicketProperties;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mobidigm on 2018-11-09.
 */
public class TicketManager extends Thread {
    private static Logger LOGGER = LogManager.getLogger();
    private String maxSequence = "0";
    RCAService service = new RCAService();

    public void run() {
        try {

            if (true) {
                return;
            }

//            Data allParam = new Data();
//            allParam.set("", null);
//            try {
//                while (true) {
//                    ArrayList<HashMap<String, Object>> allResult = service.selectList("SELECT_TICKET_CUR_LIST", allParam);
//                    if (allResult.get(0) == null) {
//                        continue;
//                    }
//                    lastTicketId = (allResult.get(0).get("ticket_id")).toString();
//                    allResult.clear();
//                    break;
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                LOGGER.log(Level.INFO, "Exception New Ticket Check  : " + e.getMessage() );
//            }

        
            updateMaxSequence(service);
            service.close();

            while (true) {
                try {
                    Data param = new Data();
                    param.set(TicketProperties.LAST_TICKET_ID, maxSequence);
                    ArrayList<HashMap<String, Object>> result = service.selectList(Constants.NIA.SQL.SELECT_TICKET_CUR_LIST, param);

                    if (result.size() > 0) {
                        broadcast(result);
                    }
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
       /* Data resultMax = service.selectOneData(Constants.NIA.SQL.SELECT_MAX_TICKET_ID, new Data().set("", null));
        Integer max = resultMax.getIntParse("max");
        if (Integer.parseInt(lastTicketId) < max) {
            lastTicketId = max.toString();
            LOGGER.log(Level.DEBUG, "LAST TICKET UPDATED : " + lastTicketId);
        }*/
    }

    private void broadcast(ArrayList<HashMap<String, Object>> result) {
        String logText = " ====== SOCKET PUSH ======>        전송티켓: No." + result.get(0).get("ticket_id");
        if (result.size() > 2) {
            logText = logText + " 외 " + (result.size() - 1) + "개";
        }
        try {
            EventBusBridge eventBusBridge = EventBusBridge.getServer();
            if(eventBusBridge != null){
                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET, new JsonObject().put("data", result));
            }
            LOGGER.log(Level.DEBUG, logText);
        } catch (NullPointerException | IllegalStateException e) {
            EventBusBridge.connectionFailed();
        }
    }
}
