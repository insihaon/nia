package com.nia.korenrca.server.http.handler;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.server.service.MainService;
import com.nia.korenrca.server.service.RCAService;
import com.nia.korenrca.service.Service;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.Request;
import com.nia.korenrca.shared.Response;
import com.nia.korenrca.shared.ServiceType;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ServiceHandler extends RequestHandler {
    private static Logger logger = LogManager.getLogger();
    public static final String Name = "/service";

    public static Router create(Router router) {
        router.route(ServiceHandler.Name).handler(new ServiceHandler());
        return router;
    }

    @Override
    protected Object handleJson(HttpServerRequest serverRequest, JsonObject json) {

        Object result = null;
        try {
            Data data = new Data();
            Utils.jsonstringToData(data, json.toString());
            if (data.getProperties().size() == 0) {
                return createErrorMessage("Invalid parameters");
            }

            String serviceName = data.get(Constants.SERVICE_NAME);
            if (serviceName == null) {
                return createErrorMessage("Invalid service name");
            }

            ServiceType serviceType = ServiceType.fromURI(serviceName.toLowerCase());
            if (serviceType == null) {
                return createErrorMessage("Invalid Url");
            }

            Request request = new Request();
            request.setMethod(serverRequest.method());
            request.setUser(user);
            request.setData(data);

            Service service = null;
            switch (serviceType) {
                case Main:
                    service = new MainService();
                    break;
                case RCA:
                    service = new RCAService();
                    break;
            }

            Response response = service.doRequest(request);
            if (response != null) {
                if (response.getDocument() != null) {
                    //response.put(Constants.ACTION_NAME, service.getAction());
                    //result = org.json.simple.JSONValue.toJSONString(response.getDocument());
                    result = response.getDocument();
                } else if (response.getMessage() != null) {
                    result = createErrorMessage(response.getMessage());
                } else {
                    result = createErrorMessage("no data");
                }
            }
        } catch (Exception e) {
            logger.error(e);
            result = createErrorMessage(e.getMessage());
        } finally {

        }

        return result;
    }
}
