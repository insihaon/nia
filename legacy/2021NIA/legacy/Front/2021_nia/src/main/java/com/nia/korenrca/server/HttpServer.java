package com.nia.korenrca.server;

import com.nia.korenrca.server.http.route.HttpRouteMatcher;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.system.DesktopSystem;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HttpServer extends AbstractVerticle {
    private static Logger logger = LogManager.getLogger(HttpServer.class.getSimpleName());

    @Override
    public void start() throws Exception {

        super.start();

        // 로걸 디버깅을 위한 프록시 서버를 이용하는 경우 Compression 을 하면 ECONNRESET 에러에 시달리게 된다.

        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setCompressionLevel(HttpServerOptions.DEFAULT_COMPRESSION_LEVEL);
        serverOptions.setCompressionSupported(DesktopSystem.IsDevelopmentMode == false && ConfigProperties.get().getWebCompressionSupported());
        serverOptions.setPort(ConfigProperties.get().getHttpServerPort());
        createHttpServer(serverOptions);

        if (!DesktopSystem.IsDevelopmentMode) {
            HttpServerOptions serverSslOptions = new HttpServerOptions();
            serverSslOptions.setCompressionSupported(DesktopSystem.IsDevelopmentMode == false && ConfigProperties.get().getWebCompressionSupported());
            serverSslOptions.setCompressionSupported(ConfigProperties.get().getWebCompressionSupported());
            serverSslOptions.setPort(ConfigProperties.get().getHttpsServerPort());
            serverSslOptions.setSsl(true);
            JksOptions jksOptions = new JksOptions();
            jksOptions.setPath(ConfigProperties.get().getKeyStorePath());
            jksOptions.setPassword(ConfigProperties.get().getKeyStorePassword());
            serverSslOptions.setKeyStoreOptions(jksOptions);
            createHttpServer(serverSslOptions);
        }
    }

    private void createHttpServer(HttpServerOptions options) {
        vertx.createHttpServer(options).requestHandler(HttpRouteMatcher.createRouteMatcher(vertx)::accept).listen(handler -> {
            if (handler.succeeded()) {
                logger.info(String.format("HTTP%s Server is running on %d", options.isSsl() ? "S" : "", options.getPort()));
            } else {
                System.err.println("Failed to listen on port " + options.getPort());
                logger.warn("Failed to listen on port ", handler.cause());
                System.exit(0);
            }
        });
    }
}
