package com.nia.korenrca.server.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nia.korenrca.shared.ConfigProperties;

import io.vertx.core.json.JsonObject;


public class SdnTokenService {
    private static Logger LOGGER = LogManager.getLogger();

    String authorization = null;
    Timer timer = null;
    private static int period = 24 * 60 * 60 * 1000;
    
    public SdnTokenService() {
        
    }

    private void start() {
        if (timer == null) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    login();
                    // timer.cancel();
                }
            };

            
            // 하루에 한 번 토큰 재발급
            timer.schedule(timerTask, period , period);
            login();
        }
    }

    public void login() {
        JsonObject result = null;
        HttpURLConnection connection = null;
        try {
            String path = "/login";
            String strUrl = "http://" + ConfigProperties.get().getSdnApiHost() + ":" + ConfigProperties.get().getSdnApiPort() + path;
            JsonObject body = new JsonObject();
            body.put("loginid", ConfigProperties.get().getSdnApiUser());
            body.put("password", ConfigProperties.get().getSdnApiPw());

            URL url = new URL(strUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
		    try (OutputStream os = connection.getOutputStream()) {
                byte request_data[] = body.toString().getBytes("utf-8");
                os.write(request_data);
                os.close();
            }

            // extract authorization from Response Header 
            authorization = connection.getHeaderFields().get("Authorization").get(0);
            LOGGER.debug(String.format("authorization=%s", authorization));

            // Response Header Print
            // for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            //     for (String value : header.getValue()) {
            //         System.out.println(header.getKey() + " : " + value);
            //     }
            // }

            // Response Body to Json
            // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // StringBuilder sb = new StringBuilder();
            // String line = null;
            // while ((line = bufferedReader.readLine()) != null) {
            //     sb.append(line);
            //     sb.append("\n");
            // }
            // bufferedReader.close();
            // result = new JsonObject(sb.toString());
            // JsonObject data = result.getJsonObject("data");
            // if(data != null) {
            //     accessToken =  data.getString("accessToken");
            //     refreshToken =  data.getString("refreshToken");
            //     LOGGER.debug(String.format("accessToken=%s, refreshToken=%s", accessToken, refreshToken));
            // }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public String getAccessToken() {
        if(timer == null) {
            this.start();
        }
        return this.authorization;
    }


    public static void main(String[] args) throws InterruptedException {
        SdnTokenService service = new SdnTokenService();

        Thread.sleep(3000);
        service.login();
        LOGGER.info(String.format("token= %s",service.getAccessToken()));
        int a = 1;
    }

}

