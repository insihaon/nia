package com.nia.korenrca.server.scheduler;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.server.service.RCAService;
import com.nia.korenrca.service.Service;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.Request;
import com.nia.korenrca.shared.Response;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import io.vertx.core.json.JsonObject;

public class QuartzJob implements Job {
    @Override public void execute(JobExecutionContext arg0) throws JobExecutionException {
        Object data = doChartRequest();
        try{
            CronTriggerRunner.defaultChartData = data;
            EventBusBridge.getServer().publish(Constants.EVENT.ADDR_OUT_BROADCAST_CHART, new JsonObject().put("data", CronTriggerRunner.defaultChartData));
        } catch(NullPointerException | IllegalStateException e) {
            EventBusBridge.connectionFailed();
        }
    }

    public Object doChartRequest() {
        JsonObject jObj = new JsonObject();
        jObj.put("service", "rca");
//        jObj.put("action", Constants.NIA.ACTION.RELOAD_RCA_CHART);
//        jObj.put("is_intercept_sq", true);

        Object result = null;
        try {
            Data data = new Data();
            Utils.jsonstringToData(data, jObj.toString());

            Request request = new Request();
            request.setUser(null);
            request.setData(data);

            Service service = new RCAService();
            Response response = service.doRequest(request);
            if (response != null) {
                if (response.getDocument() != null) {
                    result = response.getDocument();
                } else if (response.getMessage() != null) {
                    result = new JsonObject().put(Constants.JSON_ERROR_NAME, response.getMessage());
                } else {
                    result = new JsonObject().put(Constants.JSON_ERROR_NAME, "no data");
                }
            }
            return result;
        } catch (Exception e) {
            LogManager.getLogger().error(e);
            result = new JsonObject().put(Constants.JSON_ERROR_NAME, e.getMessage());
            return result;
        }
    }
}
