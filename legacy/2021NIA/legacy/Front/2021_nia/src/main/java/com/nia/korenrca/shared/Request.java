package com.nia.korenrca.shared;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.service.auth.RcaUser;
import com.nia.korenrca.service.util.Utils;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;


public class Request extends ServiceElement {
    private RcaUser user;
    private HttpMethod method;

    public Request() {
        this(null);
    }

    public Request(String command) {
        super();
        this.command = command;
    }

    public RcaUser getUser() {
        return user;
    }

    public void setUser(RcaUser user) {
        this.user = user;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Response createResponse() {
        Response response = new Response(getCommand(), true);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(JsonObject document) {
        Response response = createResponse(document, true);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(JsonObject document, boolean succeeded) {
        Response response = new Response(getCommand(), succeeded);
        response.setDocument(document);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(Data data) {
        Response response = new Response(getCommand(), true, null, data);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(ArrayList<Data> dataList) {
        Response response = new Response(getCommand(), true, null, dataList);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(String errorMessage) {
        Response response = new Response(getCommand(), errorMessage);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(boolean succeeded, String message) {
        Response response = new Response(getCommand(), succeeded, message, (Data) null);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(boolean succeeded, String message, Data data) {
        Response response = new Response(getCommand(), succeeded, message, data);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    public Response createResponse(boolean succeeded, String message, ArrayList<Data> dataList) {
        Response response = new Response(getCommand(), succeeded, message, dataList);
        response.put(Constants.SQLLOG, (String) getData().get(Constants.SQLLOG));
        response.put(Constants.INPUT, Utils.createJsonObject((Data)(getData().get(Constants.INPUT))));
        return response;
    }

    @Override
    public String toString() {
        // @formatter:off
        return super.toString();
        // @formatter:on
    }
}
