package com.nia.korenrca.server;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.server.socket.handler.BridgeEventHandler;
import com.nia.korenrca.server.socket.handler.SocketRepository;
import com.nia.korenrca.server.worker.EventProxyServer;
import com.nia.korenrca.server.worker.HeatbeatManager;
import com.nia.korenrca.server.worker.SyslogManager;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.system.DesktopSystem;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Created by Mobidigm on 2018-08-14.
 */

public class SocketServer {

    private static AbstractVerticle verticle = null;

    public static void start() {
        if (verticle == null) {
            verticle = new SocketVerticle();
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(verticle);
        }
    }
}

class SocketVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route(Constants.EVENT.EventBusRouteUri).handler(eventBusHandler());
        router.route().handler(staticHandler());

        createCors(router);

        if (vertx.createHttpServer().requestHandler(router::accept).listen(ConfigProperties.get().getSocketServerPort()) != null) {
            LOGGER.info(String.format("Socket Server is running on %d", ConfigProperties.get().getSocketServerPort()));
        }

//        new Thread(new TicketManager()).start();        // not Working
        new Thread(new HeatbeatManager()).start();
        new Thread(new SyslogManager()).start();
//        new Thread(new MonitoringManager()).start();
        EventProxyServer.start();
    }

    private SockJSHandler eventBusHandler() {

        // @formatter:off
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_MESSAGE))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_MESSAGE))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_SYSLOG))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_SYSLOG))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_HEATBEAT))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_HEATBEAT))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_DEFAULT))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_IN_DEFAULT))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_IN_SESSION))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_SESSION))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_NOTICE))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_NOTICE))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_CHART))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_CHART))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_MONITORING))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_MONITORING))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_UPDATE_STATUS))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_UPDATE_STATUS))
            .addOutboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_UNKNOWN))
            .addInboundPermitted(new PermittedOptions().setAddressRegex(Constants.EVENT.ADDR_OUT_BROADCAST_UNKNOWN))
            ;
        // @formatter:on

        if (DesktopSystem.IsDevelopmentMode) {
            long timeout = 300 * 1000L;
            options.setPingTimeout(timeout).setReplyTimeout(timeout);
        }

        SharedData data = vertx.sharedData();
        SocketRepository repository = new SocketRepository(data);
        EventBus eventBus = vertx.eventBus();
        BridgeEventHandler bridgeEventHandler = new BridgeEventHandler(eventBus, repository);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
//        SoWHERE NOT EXISTS(SELECT * FROM UPSERT)ckJSHandler sockJSHandler = SockJSHandler.create(vertx, new SockJSHandlerOptions().setMaxBytesStreaming(Integer.MAX_VALUE));
        return sockJSHandler.bridge(options, bridgeEventHandler);
    }

    private StaticHandler staticHandler() {
        return StaticHandler.create().setCachingEnabled(false);
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


