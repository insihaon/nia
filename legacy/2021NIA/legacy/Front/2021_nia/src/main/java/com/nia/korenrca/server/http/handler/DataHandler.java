package com.nia.korenrca.server.http.handler;

import com.nia.korenrca.server.service.MainService;
import com.nia.korenrca.service.Service;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.Request;
import com.nia.korenrca.shared.Response;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DataHandler extends RequestHandler {
    private static Logger LOGGER = LogManager.getLogger();
    public static final String Name = "/data";

    public static Router create(Router router) {
        router.route(DataHandler.Name).handler(new DataHandler());
        return router;
    }

    @Override
    protected boolean isNeedAuth() {
        return false;
    }

    @Override
  protected Object handleJson(HttpServerRequest serverRequest, JsonObject json) {
        Object result = null;
        try {
      Data data = new Data();
      Utils.jsonstringToData(data, json.toString());

      Request request = new Request();
      request.setMethod(serverRequest.method());
      request.setUser(user);
      request.setData(data);

      Service service = new MainService(this.context);
      Response response = service.doRequest(request);
      if (response != null) {
        if (response.getDocument() != null) {
          //response.put(Constants.ACTION_NAME, service.getAction());
          result = response.getDocument();
        } else if (response.getMessage() != null) {
          result = createErrorMessage(response.getMessage());
        } else {
          result = createErrorMessage("no data");
        }
      }
        } catch (Exception e) {
            LOGGER.error(e);
            result = createErrorMessage(e.getMessage());
        }

        return result;
    }
}
