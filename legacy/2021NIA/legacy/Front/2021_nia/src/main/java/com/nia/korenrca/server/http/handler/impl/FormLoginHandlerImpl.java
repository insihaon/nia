/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package com.nia.korenrca.server.http.handler.impl;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.server.http.handler.FormLoginHandler;
import com.nia.korenrca.service.cipher.rsa.RSA;
import com.nia.korenrca.service.cipher.rsa.RSA.RSAKey;
import com.nia.korenrca.service.cipher.tea.TEA;
import com.nia.korenrca.service.util.Base64;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.apache.logging.log4j.LogManager;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class FormLoginHandlerImpl implements FormLoginHandler {

    private static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    private final AuthProvider authProvider;

    private String usernameParam;
    private String passwordParam;
    private String returnURLParam;
    private String directLoggedInOKURL;

    @Override
    public FormLoginHandler setUsernameParam(String usernameParam) {
        this.usernameParam = usernameParam;
        return this;
    }

    @Override
    public FormLoginHandler setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
        return this;
    }

    @Override
    public FormLoginHandler setReturnURLParam(String returnURLParam) {
        this.returnURLParam = returnURLParam;
        return this;
    }

    @Override
    public FormLoginHandler setDirectLoggedInOKURL(String directLoggedInOKURL) {
        this.directLoggedInOKURL = directLoggedInOKURL;
        return this;
    }

    public FormLoginHandlerImpl(AuthProvider authProvider, String usernameParam, String passwordParam, String returnURLParam, String directLoggedInOKURL) {
        this.authProvider = authProvider;
        this.usernameParam = usernameParam;
        this.passwordParam = passwordParam;
        this.returnURLParam = returnURLParam;
        this.directLoggedInOKURL = directLoggedInOKURL;
    }

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest req = context.request();
        if (req.method() != HttpMethod.POST) {
            context.fail(405); // Must be a POST
        } else {
            if (!req.isExpectMultipart()) {
                throw new IllegalStateException("Form body not parsed - do you forget to include a BodyHandler?");
            }

			req.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");

			Session session = context.session();
            MultiMap params = req.formAttributes();
            final boolean servicemode;
            String encData = params.get("data");
            String username;
            String password;
            if (encData != null) {
                servicemode = true;
                RSAKey rsaKey = session.get(Constants.NIA.RSA_KEY);
                JsonObject encJson = new JsonObject(encData);
                String encryptKey = encJson.getString("key");
                String encryptValue = Base64.decodeString(encJson.getString("value"));
                //        System.out.println("key: " + encryptKey);
                //        System.out.println("value: " + encryptValue);

                /* TEA 암호화에 사용된 키를 구한다 */
                String teaKey = null;
                try {
                    teaKey = RSA.decryptRsa(rsaKey, encryptKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* 암호화된 text를 TEA키를 이용하여 decrypt 한다. */
                TEA tea = new TEA(teaKey);
                String data = tea.decrypt(encryptValue);
                JsonObject json = new JsonObject(data);
                username = json.getString(usernameParam);
                password = json.getString(passwordParam);
            } else {
                servicemode = false;
                username = params.get(usernameParam);
                password = params.get(passwordParam);
            }

            if (username == null || password == null) {
                LOGGER.warn("No username or password provided in form - did you forget to include a BodyHandler?");
                //context.fail(400);
                req.response().end("No username or password provided");
            } else {
                //Session session = context.session();
                JsonObject authInfo = new JsonObject().put("username", username).put("password", password);
                authProvider.authenticate(authInfo, res -> {
                    if (res.succeeded()) {
                        User user = res.result();
                        context.setUser(user);
                        if (session != null) {
                            // the user has upgraded from unauthenticated to authenticated
                            // session should be upgraded as recommended by owasp
                            session.regenerateId();

                            String returnURL = session.remove(returnURLParam);
                            if (!servicemode && returnURL != null) {
                                // Now redirect back to the original url
                                doRedirect(req.response(), returnURL);
                                return;
                            }
                        }
                        // Either no session or no return url
                        if (!servicemode && directLoggedInOKURL != null) {
                            // Redirect to the default logged in OK page - this would occur
                            // if the user logged in directly at this URL without being redirected here first from another
                            // url
                            doRedirect(req.response(), directLoggedInOKURL);
                        } else {
                            // Just show a basic page
                            String json = user.principal().put("message", DEFAULT_DIRECT_LOGGED_IN_OK_PAGE).toString();
                            req.response().end(json);
                        }
                    } else {
                        //context.fail(403); // Failed login
						req.response().end(res.cause().toString().replace("io.vertx.core.impl.NoStackTraceThrowable: ", ""));
                    }
                });
            }
        }
    }

    private void doRedirect(HttpServerResponse response, String url) {
        response.putHeader(HttpHeaders.LOCATION, url).setStatusCode(302).end();
    }

    private static final String DEFAULT_DIRECT_LOGGED_IN_OK_PAGE = "Login successful";
}
