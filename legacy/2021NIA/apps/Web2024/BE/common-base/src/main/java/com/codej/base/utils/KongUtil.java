package com.codej.base.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

@Slf4j
@Component
public class KongUtil {
    @Value("${myconf.api-server.kong-admin:http://192.168.0.222:8001}")
    private String KONG_ADMIN_URL;
    
    @Value("${myconf.api-server.kong:http://192.168.0.222:8000/}")
    private String KONG_API_URL;

    @Value("${myconf.api-server.hub-api:http://127.0.0.1:8070/dh}")
    private String hubUrl;

    private static final OkHttpClient client = new OkHttpClient();

    private String getKongAdminUrl() {
        return KONG_ADMIN_URL == null ? "http://127.0.0.1:8001" : KONG_ADMIN_URL;
    }

    private String getKongApiUrl() {
        return KONG_API_URL == null ? "http://127.0.0.1:8000/" : KONG_API_URL;
    }
    private void logCurlCommand(Request request) throws IOException {
        StringBuilder curlCommand = new StringBuilder("curl -X ");
        curlCommand.append(request.method()).append(" ").append(request.url()).append(" ");
    
        Headers headers = request.headers();
        for (String name : headers.names()) {
            curlCommand.append("-H \"").append(name).append(": ").append(headers.get(name)).append("\" ");
        }
    
        if (request.method().equalsIgnoreCase("POST") || request.method().equalsIgnoreCase("PUT")) {
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                curlCommand.append("-d '").append(buffer.readUtf8()).append("'");
            }
        }
    
        log.info("$ " + curlCommand.toString());
    }
    

    public Response createService(String serviceName, String serviceUrl) throws IOException {
        // MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        // $ curl -X POST http://localhost:8001/services -d "name=ALARM" -d "url=http://host.docker.internal:9000/alarm.json"
        RequestBody body = new FormBody.Builder()
                .add("name", serviceName)
                .add("url", String.format("%s/%s", hubUrl, serviceUrl.replaceFirst("^/+", "")))
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public String createHost(String serviceName, String hostName) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("hosts[]", hostName)
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName + "/routes/")
                .post(body)
                .build();
    
        logCurlCommand(request); // Log the curl command
    
        Response response = client.newCall(request).execute();
    
        // Check for success and parse the response to get the route key
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(responseBody);
    
            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                String routeKey = jsonObject.get("id").getAsString();
                log.info("route_key: " + routeKey);
                log.info("Host created successfully");
                return routeKey;
            }
        } else {
            log.info("Error creating host: " + response.message());
        }
    
        return null;
    }

    public Response updateServiceUrl(String serviceName, String newUrl) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("url", newUrl)
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName)
                .patch(body)
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public Response enableKeyAuthPlugin(String serviceName) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("name", "key-auth")
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName + "/plugins")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public Response createConsumer(String consumerName) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("username", consumerName)
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public String createConsumerApiKey(String consumerName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerName + "/key-auth")
                .post(RequestBody.create(null, new byte[0]))
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to extract the API key
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        String apiKey = null;
        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            apiKey = jsonObject.get("key").getAsString();
        }
        return apiKey;
    }

    public Response requestService(String serviceUrl) throws IOException {
        Request request = new Request.Builder()
                .url(serviceUrl)
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public Response requestNonAuthenticatedService(String hostName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongApiUrl())
                .header("Host", hostName)
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public Response requestAuthenticatedService(String apiKey, String hostName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongApiUrl())
                .header("apikey", apiKey)
                .header("Host", hostName)
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public Response getAllRoutes() throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/routes")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

    public Response enableKeyAuthPluginForRoute(String routeId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("name", "key-auth")
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/routes/" + routeId + "/plugins")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        return client.newCall(request).execute();
    }

        public void deleteConsumerKey(String consumerName, String apiKey) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerName + "/key-auth/" + apiKey)
                .delete()
                .build();

        logCurlCommand(request); // Log the curl command

        client.newCall(request).execute();
    }

    public void deleteConsumer(String consumerId) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerId)
                .delete()
                .build();

        logCurlCommand(request); // Log the curl command

        client.newCall(request).execute();
    }

    public void deleteHost(String serviceName) throws IOException {
        List<String> routeKey_lst = getRouteKeysByService(serviceName);

        for (String routeKey : routeKey_lst) {
            log.info("Deleting associated route " + serviceName + ", " + routeKey);

            Request request = new Request.Builder()
                    .url(getKongAdminUrl() + "/services/" + serviceName + "/routes/" + routeKey)
                    .delete()
                    .build();

            logCurlCommand(request); // Log the curl command

            client.newCall(request).execute();
        }
    }

    public void deleteService(String serviceName) throws IOException {
        log.info("Deleting service " + serviceName);

        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName)
                .delete()
                .build();

        logCurlCommand(request); // Log the curl command

        client.newCall(request).execute();
    }

    public String getApiKeyByConsumerName(String consumerName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerName + "/key-auth")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to extract the API key
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        String apiKey = null;
        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray consumersArray = jsonObject.getAsJsonArray("data");

            for (JsonElement consumerElement : consumersArray) {
                JsonObject consumerObject = consumerElement.getAsJsonObject();
                if (consumerObject.has("key")) {
                    apiKey = consumerObject.get("key").getAsString();
                    return apiKey;
                }
            }
        }

        return apiKey; // Handle the case when no API key is found
    }

    public String getRouteKeyByHostAndService(String hostName, String serviceName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName + "/routes")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to find the route key by host and service
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");

            for (JsonElement routeElement : data) {
                JsonObject routeObject = routeElement.getAsJsonObject();
                JsonArray hosts = routeObject.getAsJsonArray("hosts");

                if (hosts != null && !hosts.isJsonNull() && hosts.size() > 0) {
                    for (JsonElement hostElement : hosts) {
                        String host = hostElement.getAsString();
                        if (hostName.equals(host)) {
                            String routeKey = routeObject.get("id").getAsString();
                            return routeKey;
                        }
                    }
                }
            }
        }

        return null; // Handle the case when no route key is found
    }

    public void deleteAllConsumers() throws IOException {
        try {
            // Get all consumers
            Request request = new Request.Builder()
                    .url(getKongAdminUrl() + "/consumers")
                    .get()
                    .build();

            logCurlCommand(request); // Log the curl command

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // Parse the JSON response to get a list of consumers
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(responseBody);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray consumersArray = jsonObject.getAsJsonArray("data");

                for (JsonElement consumerElement : consumersArray) {
                    JsonObject consumerObject = consumerElement.getAsJsonObject();
                    String consumerId = consumerObject.get("id").getAsString();
                    log.info("Deleting consumer " + consumerId);
                    deleteConsumer(consumerId);
                }
            }
        } catch (IOException e) {
            log.info("Error deleting consumers: " + e.getMessage());
        }
    }

    public void deleteAllServices() throws IOException {
        try {
            // Get all services
            Request request = new Request.Builder()
                    .url(getKongAdminUrl() + "/services")
                    .get()
                    .build();

            logCurlCommand(request); // Log the curl command

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // Parse the JSON response to get a list of services
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(responseBody);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray servicesArray = jsonObject.getAsJsonArray("data");

                for (JsonElement serviceElement : servicesArray) {
                    JsonObject serviceObject = serviceElement.getAsJsonObject();
                    String serviceName = serviceObject.get("name").getAsString();
                    log.info("Deleting service " + serviceName);
                    deleteHost(serviceName);
                    deleteService(serviceName);
                }
            }
        } catch (IOException e) {
            log.info("Error deleting services: " + e.getMessage());
        }
    }

    public boolean existService(String serviceName) {
        try {
            Request request = new Request.Builder()
                    .url(getKongAdminUrl() + "/services/" + serviceName)
                    .get()
                    .build();

            logCurlCommand(request); // Log the curl command

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // Parse the JSON response to find the route keys for the service
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(responseBody);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                if (jsonObject.has("id")) {
                    return true;
                } 
            }
        } catch (Exception e) {
            // Handle any exceptions or errors here
        }
        return false;
    }

    public List<String> getRouteKeysByService(String serviceName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName + "/routes")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to find the route keys for the service
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        List<String> routeKeys = new ArrayList<>();

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");

            for (JsonElement routeElement : data) {
                JsonObject routeObject = routeElement.getAsJsonObject();
                String routeKey = routeObject.get("id").getAsString();
                routeKeys.add(routeKey);
            }
        }

        return routeKeys;
    }

    public String createAclGroup(String consumerName, String aclGroupName) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("group", aclGroupName)
                .add("consumer", "{\"username\":\"" + consumerName + "\"}")
                .build();

        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/acls")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to get the ID of the created ACL group
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            if (jsonObject.has("id")) {
                String aclGroupId = jsonObject.get("id").getAsString();
                return aclGroupId;
            }
        }

        return null; // Handle the case when the ACL group is not created successfully
    }

    public List<String> getAclsForConsumer(String consumerId) throws IOException {
        List<String> aclInfo = new ArrayList<>();

        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerId + "/acls")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to extract ACL information for the consumer
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");

            for (JsonElement aclElement : data) {
                JsonObject aclObject = aclElement.getAsJsonObject();
                String aclName = aclObject.get("group").getAsString();
                String aclId = aclObject.get("id").getAsString();
                aclInfo.add("ACL Name: " + aclName + ", ACL ID: " + aclId);
            }
        }

        return aclInfo;
    }

    public void addLinkConsumerWithAclGroup(String consumerName, String aclGroupName) throws IOException {
        String aclGroupId = getAclGroupId(aclGroupName);

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("group", aclGroupName)
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerName + "/acls")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();

        // Handle the response as needed
    }

    public void deleteLinkConsumerWithAclGroup(String consumerName, String aclGroupName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers/" + consumerName + "/acls/" + aclGroupName)
                .delete()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();

        // Handle the response as needed
    }

    public String getAclGroupId(String aclGroupName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/acls?name=" + aclGroupName)
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to get the ID of the ACL group by name
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");

            if (data.size() > 0) {
                JsonObject aclGroupObject = data.get(0).getAsJsonObject(); // Assuming the first ACL group
                String aclGroupId = aclGroupObject.get("id").getAsString();
                return aclGroupId;
            }
        }

        return null; // Handle the case when no ACL group with the specified name is found
    }

    public void addAclPluginToService(String serviceName, String aclGroupName) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("name", "acl")
                .add("config.allow", aclGroupName)
                .build();
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services/" + serviceName + "/plugins")
                .post(body)
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();

        // Handle the response as needed
    }

    public List<String> getAllHosts() throws IOException {
        List<String> hosts = new ArrayList<>();

        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/services?size=1000")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to get all hosts from services
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");

            for (JsonElement serviceElement : data) {
                JsonObject serviceObject = serviceElement.getAsJsonObject();
                String serviceId = serviceObject.get("id").getAsString();
                String serviceName = serviceObject.get("name").getAsString();

                Request routeRequest = new Request.Builder()
                        .url(getKongAdminUrl() + "/services/" + serviceId + "/routes")
                        .get()
                        .build();

                Response routeResponse = client.newCall(routeRequest).execute();
                String routeResponseBody = routeResponse.body().string();

                JsonElement routeRoot = jsonParser.parse(routeResponseBody);

                if (routeRoot.isJsonObject()) {
                    JsonObject routeObject = routeRoot.getAsJsonObject();
                    JsonArray routes = routeObject.getAsJsonArray("data");

                    for (JsonElement routeElement : routes) {
                        JsonObject route = routeElement.getAsJsonObject();
                        JsonArray hostsArray = route.getAsJsonArray("hosts");

                        if (hostsArray != null) {
                            for (JsonElement hostElement : hostsArray) {
                                String host = hostElement.getAsString();
                                hosts.add(serviceName + ", " + host);
                            }
                        }
                    }
                }
            }
        }

        return hosts;
    }

    public int[] getAllApiKeyResponses() throws IOException {
        int total = 0;
        int success = 0;

        // Create a set to keep track of processed combinations of apiKey and hostName
        Set<String> processedCombinations = new HashSet<>();

        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers")
                .get()
                .build();

        logCurlCommand(request); // Log the curl command

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Parse the JSON response to get a list of consumers
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);

        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray consumersArray = jsonObject.getAsJsonArray("data");

            for (JsonElement consumerElement : consumersArray) {
                JsonObject consumerObject = consumerElement.getAsJsonObject();
                String consumerName = consumerObject.get("username").getAsString();
                String consumerId = consumerObject.get("id").getAsString();

                String apiKey = getApiKeyByConsumerName(consumerName);

                List<String> serviceHosts = getAllHosts();

                if (serviceHosts != null && !serviceHosts.isEmpty()) {
                    for (String serviceHost : serviceHosts) {
                        String[] parts = serviceHost.split(", ");
                        String serviceName = parts[0];
                        String hostName = parts[1];

                        // Create a unique key for this combination
                        String combinationKey = apiKey + "|" + hostName;

                        // Check if this combination has already been processed
                        if (!processedCombinations.contains(combinationKey)) {
                            total++;
                            Headers headers = new Headers.Builder()
                                    .add("apikey", apiKey)
                                    .add("Host", hostName)
                                    .build();

                            Request apiRequest = new Request.Builder()
                                    .url(getKongApiUrl())
                                    .headers(headers)
                                    .get()
                                    .build();

                            try {
                                logCurlCommand(apiRequest); // Log the curl command

                                Response apiResponse = client.newCall(apiRequest).execute();

                                if (apiResponse.isSuccessful()) {
                                    log.info("Success - Service: " + serviceName + ", Host: " + hostName + ", Consumer: " + consumerName + ", API Key: " + apiKey);
                                    success++;
                                    // Process the response body if required
                                } else {
                                    log.info("Failure - Service: " + serviceName + ", Host: " + hostName + ", Consumer: " + consumerName + ", API Key: " + apiKey);
                                    // Handle the failure or error response
                                }
                            } catch (IOException e) {
                                log.info("Failure - Service: " + serviceName + ", Host: " + hostName + ", Consumer: " + consumerName + ", API Key: " + apiKey + ", Error: " + e.getMessage());
                                // Handle the exception
                            }

                            // Add the combination to the set of processed combinations
                            processedCombinations.add(combinationKey);
                        }
                    }
                }
            }
        }

        log.info("*** 결과 - " + success + "/" + total);
        return new int[]{success, total};
    }

    public String getConsumerIdByName(String consumerName) throws IOException {
        Request request = new Request.Builder()
                .url(getKongAdminUrl() + "/consumers")
                .get()
                .build();
    
        // Log the curl command
        logCurlCommand(request);
    
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
    
        // Parse the JSON response to get the consumer ID by name
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(responseBody);
    
        if (root.isJsonObject()) {
            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");
    
            for (JsonElement consumerElement : data) {
                JsonObject consumerObject = consumerElement.getAsJsonObject();
                String username = consumerObject.get("username").getAsString();
                String consumerId = consumerObject.get("id").getAsString();
    
                if (consumerName.equals(username)) {
                    return consumerId;
                }
            }
        }
    
        return null; // Handle the case when no consumer with the specified name is found
    }
    
}
