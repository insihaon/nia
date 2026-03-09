package com.nia.korenrca.server.http.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.service.auth.RcaAuthImpl;
import com.nia.korenrca.service.auth.RcaUser;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.shared.properties.rca.UserProperties;
import com.nia.korenrca.system.DesktopSystem;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public abstract class RequestHandler implements Handler<RoutingContext> {
    private static Logger logger = LogManager.getLogger();
    protected RoutingContext context;
    protected RcaUser user;

    protected RequestHandler() {
        super();
    }

    protected boolean isNeedAuth() {
        return ConfigProperties.get().getAuthUse();
    }

    @Override
    public void handle(RoutingContext context) {

        Vertx vertx = context.vertx();
        WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool", 10, 10 * 60 * 1000);
        executor.executeBlocking(future -> {

            this.context = context;
            HttpServerRequest request = context.request();
            user = (RcaUser) context.user();

            // 클라이언트에서 서버 접속이 자동으로 끊기고 로그인화면으로 라우팅된다. 
            // 이 문제를 해결하기 위해 강제로 user 를 생성/설정 한다.
            if(user == null) {
                user = RcaAuthImpl.getDefaultUser();
                logger.debug("context.user is null. defaultUser: {}", user.toString());
            }

            // if(user == null){
            //     logger.debug("context.user: null");
            // } else {
            //     logger.debug("context.user: {}", user.toString());
            // }
            if (isNeedAuth() && user == null) {
                handleAuth(request);
            } else {
                request.response().putHeader("auth", String.valueOf(ConfigProperties.get().getAuthUse() == false || user != null));

                MultiMap headers = request.headers();
                MultiMap params = request.params();
                @SuppressWarnings("unused")
                String encrypt = headers.get("x-encrypt");

                String parameters = context.getBody().getString(0, context.getBody().length());

                // logger.debug("headers:{}, params:{}, body:{}", headers, params, parameters);

                JsonObject json = null;
                if (parameters == null || parameters.isEmpty() || parameters.equals("undefined")) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for (Entry<String, String> entry : params.entries()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        map.remove(key);
                        map.put(key, value);
                    }
                    json = new JsonObject(map);
                } else {
                    json = Utils.createJsonObject(parameters);
                    if (json == null) {
                        Map<String, Object> map = Utils.splitQuery(parameters);
                        json = new JsonObject(map);
                    } else {
                        json.put("is_intercept_sql", "true".equals(headers.get("is_intercept_sql")));
                    }
                }

                boolean dev = !isNeedAuth() || DesktopSystem.IsDevelopmentMode || Utils.IsDev();
                if (user != null) {
                    if (!dev || !json.containsKey(UserProperties.LVL)) {
                        json.put(UserProperties.LVL, user.getLevel());
                    }
                }

                // JsonObject body = context.getBodyAsJson();
                // Object response = handleJson(request, context.getBodyAsJson());
                Object response = handleJson(request, json);
                if (response != null) {
                    if (!request.response().headers().contains(HttpHeaders.CONTENT_TYPE)) {
                        String contentType = "";
                        if (response instanceof JsonObject) {
                            contentType += "application/json;";
                        }
                        contentType += "charset=utf-8;";
                        request.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType);
                    }

                    request.response().end(String.valueOf(response));
                } else {
                    request.response().end();
                }

                request.response().closed();
            }

            future.complete();
        }, false, res -> {
            executor.close();
        });
    }

    protected void handleAuth(HttpServerRequest request) {
        // context.fail(403);
        request.response().end(createErrorMessage("Forbidden").encodePrettily());
        request.response().closed();
    }

    protected abstract Object handleJson(HttpServerRequest request, JsonObject json);

    public JsonObject createErrorMessage(String message) {
        return new JsonObject().put(Constants.JSON_ERROR_NAME, message);
    }
}
