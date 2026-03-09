package com.nia.korenrca.server.http.route;

import com.nia.korenrca.server.http.handler.AuthHandler;
import com.nia.korenrca.server.http.handler.DataHandler;
import com.nia.korenrca.server.http.handler.ServiceHandler;
import com.nia.korenrca.server.http.handler.UploadHandler;
import com.nia.korenrca.server.http.handler.RestfulApi.TsdnReserveHandler;
import com.nia.korenrca.server.http.handler.RestfulApi.TsdnServiceHandler;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.system.DesktopSystem;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class HttpRouteMatcher {
    private static Logger LOGGER = LogManager.getLogger();
    public static String WEB_PATH;
    public static String ACCESS_PATH;

    public static Router createRouteMatcher(Vertx vertx) {
        WEB_PATH = ConfigProperties.get().getWebPath();
        ACCESS_PATH = ConfigProperties.get().getAccessPath();

        Router router = Router.router(vertx);

        // We need cookies, sessions and request bodies
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create(ACCESS_PATH));

        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setSessionTimeout(Long.MAX_VALUE));

        router.route().handler(ctx -> {
            ctx.response()
                .putHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate")
                .putHeader("Pragma", "no-cache")
                .putHeader(HttpHeaders.EXPIRES, "0");
//                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
//            ctx.response().headers().set(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");


            String path = ctx.request().path();
            LOGGER.warn("dev: " + DesktopSystem.IsDevelopmentMode + ", " + path + ", " + ctx.request().remoteAddress() + ", " + ctx.getBody());
            ctx.next();

            ctx.request().connection().exceptionHandler(ex -> {
                if (ex instanceof IOException) {
                    if ("현재 연결은 원격 호스트에 의해 강제로 끊겼습니다".equals(ex.getMessage())) {
                        return;
                    }
                }
                LOGGER.error(ex);
            });
        });

        createCors(router);

        if (DesktopSystem.IsAuthPassMode != true) {
            AuthHandler.create(vertx, router);
        }
        ServiceHandler.create(router);
        DataHandler.create(router);
        UploadHandler.create(router);
        TsdnReserveHandler.create(router);
        TsdnServiceHandler.create(router);

        router.route().failureHandler(req -> {
            if (req.statusCode() == -1) {
                LOGGER.warn(Utils.getStackTrace(req.failure()));
                req.next();
                return;
            }

            req.response().setStatusCode(req.statusCode());
            switch (req.statusCode()) {
                case 401:
                    req.response().end("UNAUTHORIZED_BODY");
                    break;

                case 403:
                    req.response().end("FORBIDDEN_BODY");
                    //Utils.doRedirect(req.response(), "/loginpage.html?status=forbidden");
                    break;

                default:
                    req.response().end("Unexpected error");
            }
        });

        router.route().handler(StaticHandler.create(WEB_PATH));
        router.route().handler(StaticHandler.create(ACCESS_PATH));

        return router;
    }

    private static void createCors(Router router) {
        // CORS support
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("X-Requested-With");
        allowHeaders.add("Content-Type");
        allowHeaders.add("Accept");
        allowHeaders.add("Origin");
        allowHeaders.add("x-encrypt");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.OPTIONS);
        allowMethods.add(HttpMethod.PUT);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));
    }
}
