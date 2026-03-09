package com.nia.korenrca.server.http.handler;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.server.service.MainService;
import com.nia.korenrca.service.auth.RcaAuthImpl;
import com.nia.korenrca.service.cipher.rsa.RSA;
import com.nia.korenrca.service.cipher.rsa.RSA.RSAKey;
import com.nia.korenrca.shared.ConfigProperties;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.UserSessionHandler;


public class AuthHandler {

    public static Router create(Vertx vertx, Router router) {
        JDBCClient client = JDBCClient.createShared(vertx, new JsonObject().put("url", ConfigProperties.get().getDbUrl()).put("driver_class", ConfigProperties.get().getDbDriver())
            .put("user", ConfigProperties.get().getDbUsername()).put("password", ConfigProperties.get().getDbPassword()));

        // Simple auth service which uses a JDBC data source
        AuthProvider authProvider = RcaAuthImpl.create(vertx, client, new MainService());

        // We need a user session handler too to make sure the user is stored in the session between requests
        router.route().handler(UserSessionHandler.create(authProvider));

        final String loginUrl = "/";// + (Utils.IsDev() ? "?debug=true" : "");//"/login.html";

        router.route("/logincheck").handler(context -> {
            context.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8;");
            context.response().end("user: " + (context.user() == null ? "null" : context.user().principal()));
        });

        FormLoginHandler formLoginHandler = FormLoginHandler.create(authProvider);
        formLoginHandler.setDirectLoggedInOKURL("/");
        router.route("/loginhandler").handler(formLoginHandler);

        // Implement logout
        router.route("/logout").handler(context -> {
            context.clearUser();
            // Redirect back to the index page
            context.response().putHeader(HttpHeaders.LOCATION, loginUrl).setStatusCode(302).end();
        });

        router.route(HttpMethod.POST, "/getkey").handler(context -> {
            Session session = context.session();
            if (session.get(Constants.NIA.RSA_KEY) == null) {
                session.put(Constants.NIA.RSA_KEY, RSA.createRsa());
            }
            RSAKey rsaKey = session.get(Constants.NIA.RSA_KEY);
            JsonObject json = new JsonObject();
            json.put("m", rsaKey.getPublicKeyModulus());
            json.put("e", rsaKey.getPublicKeyExponent());

            context.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
            context.response().end(json.encode());
        });

        return router;
    }
}
