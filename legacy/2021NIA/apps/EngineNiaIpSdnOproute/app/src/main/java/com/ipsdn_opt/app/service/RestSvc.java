package com.ipsdn_opt.app.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestSvc {
    private List<String> cookies;
    private String hostAddrPort;
    private String newHostAddrPort;
    private String jwtToken;
    public RestSvc() {
        this.cookies = controllerLogin("http://203.255.249.31:8088");
        this.jwtToken = newControllerLogin("http://203.255.249.31:9088");
    }

    public List<String> controllerLogin(String hostAddrPort){
        this.hostAddrPort = hostAddrPort;
        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject params = new JSONObject();
        params.put("loginid", "test1");
        params.put("password", "1234");

        HttpEntity<?> tokenRequest =
                new HttpEntity<String>(params.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                hostAddrPort + "/ipsdn/auth/login",
                HttpMethod.POST,
                tokenRequest,
                Map.class
        );
        return response.getHeaders().get("Set-Cookie");
    }
    public String newControllerLogin(String hostAddrPort) {
        this.newHostAddrPort = hostAddrPort;
        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject params = new JSONObject();
        params.put("loginid", "test1");
        params.put("password", "1234");

        HttpEntity<?> tokenRequest =
                new HttpEntity<String>(params.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                hostAddrPort + "/login",
                HttpMethod.POST,
                tokenRequest,
                Map.class
        );

        return response.getHeaders().get("Authorization").get(0);
    }
    public JSONObject requestToRestServer(String uri, String method, Object requestBody) throws Exception {
        JSONObject result;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders header = new HttpHeaders();
            for(String cookie : cookies) {
                header.add("Cookie", cookie);
            }
            HttpEntity<?> entity;
            if(requestBody==null)
                entity = new HttpEntity<>(header);
            else
                entity = new HttpEntity<>(requestBody, header);
            ResponseEntity<Map> resultMap;
            if(method.toUpperCase().equals("GET"))
                resultMap = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            else if(method.toUpperCase().equals("POST"))
                resultMap = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
            else if(method.toUpperCase().equals("DELETE"))
                resultMap = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Map.class);
            else if(method.toUpperCase().equals("PUT"))
                resultMap = restTemplate.exchange(uri, HttpMethod.PUT, entity, Map.class);
            else throw new Exception("Method is inappropriated. required as follows [GET, POST, DELETE, PUT]");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(resultMap.getBody());
            result = new JSONObject(jsonString);
        } catch(Exception e) {
            throw e;
        }
        return result;
    }
    public List<String> getCookies() {
        return this.cookies;
    }
    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }
    public String getHostAddrPort() {
        return this.hostAddrPort;
    }
    public String getNewHostAddrPort() {
        return this.newHostAddrPort;
    }
    public String getToken() {
        return this.jwtToken;
    }
}
