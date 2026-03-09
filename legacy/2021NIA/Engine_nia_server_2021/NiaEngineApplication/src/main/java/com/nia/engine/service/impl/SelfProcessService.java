package com.nia.engine.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nia.engine.common.Response;
import com.nia.engine.vo.selfProcess.TicketAlCurrentVo;
import com.nia.engine.vo.syslog.SyslogAlarmVo;
import org.codehaus.jettison.json.JSONObject;
//import com.google.gson.JsonObject;
import com.nia.engine.common.SSHSession;
import com.nia.engine.common.Utils;
import com.nia.engine.mapper.AutoProcessMapper;
import com.nia.engine.vo.selfProcess.AutoTreatProcessVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Component
@Scope(value = "prototype")
public class SelfProcessService {
    // 연관 정보 : tb_auto_treat_process_dtl
    private final Logger LOGGER = Logger.getLogger(SelfProcessService.class);

    @Autowired
    private AutoProcessMapper autoProcessMapper;

    @Autowired
    private SSHSession session;

//자가최적화
    public void UserTicketFeedbackCheck(String serviceUrl, String num){
//    - 마감 될 때 tb_auto_treat_process_dtl 마감시간 찍어주기
//      "ipsdn/services/config/interfaces/shutdown";
        AutoTreatProcessVo autoTreatProcessVo = autoProcessMapper.selectAutoProcess(num);
        HashMap<String, String> autoProcessUpdateMap;
        TicketAlCurrentVo ticketAlCurrentVo = autoProcessMapper.selectTicketAlCurrent(num);
        try {
//          마감 타입 '자동' 일 시 port down 원격 제어
            if ("A".equals(autoTreatProcessVo.getClosType())) {
                requestNiaApi(serviceUrl);
                LOGGER.info("[SelfProcessService] UserTicketFeedbackCheck port_down ===> " +autoTreatProcessVo.getTicketId());
            }

//          원격제어 시 마감 상태 'Y'로 현황 업데이트
            autoProcessUpdateMap = new HashMap<>();
            autoProcessUpdateMap.put("ticketId",num);
            autoProcessMapper.updateAutoProcessTicket(autoProcessUpdateMap);
            LOGGER.info("[SelfProcessService] UserTicketFeedbackCheck status update =====> "+autoTreatProcessVo.getTicketId());


        }catch (Exception e){
            LOGGER.error(e);
        }
    }
    public void UserSyslogFeedbackCheck(String serviceUrl,String num){
//    - 마감 될 때 tb_auto_treat_process_dtl 마감시간 찍어주기
//        "ipsdn/services/config/interfaces/shutdown"; "ipsdn/services/config/interfaces/noshut";
        AutoTreatProcessVo autoTreatProcessVo = autoProcessMapper.selectAutoProcess(num);
        HashMap<String, String> autoProcessUpdateMap;
//        SyslogAlarmVo syslogAlarmVo = autoProcessMapper.selectNodeIfName(Hnum);
        try {
            if ("A".equals(autoTreatProcessVo.getClosType())) {
                requestNiaApi(serviceUrl);
                LOGGER.info("[SelfProcessService] UserSyslogFeedbackCheck port_down ===> " +autoTreatProcessVo.getTicketId());
            }

            //현황 업데이트(마감시간,마감상태)
            autoProcessUpdateMap = new HashMap<>();
            autoProcessUpdateMap.put("alarmno",num);
            autoProcessMapper.updateAutoProcessSyslog(autoProcessUpdateMap);

        }catch (Exception e){
            LOGGER.error(e);
        }
    }

    public void requestNiaApi(String apiUrl) {
        try {
//            String token = loginAndGetToken("codej","codej!@#");
            String token = "accessToken=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImNvZGVqIiwiaWF0IjoxNzAxNzQyNDkxLCJleHAiOjE3MDQzMzQ0OTF9.qS9dt5Q8K0ukiabMmFlKbXHzt813JbZCW9c0HqxExRI; refreshToken=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImNvZGVqIiwiaWF0IjoxNzAxNzQyNDkxLCJleHAiOjE3MDQ0MjA4OTF9.DC5SmEsj35aIMayiyP6rKtsu1v8ClQJiYpbHH_9q6os";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            System.out.println("[requestNiaApi] accessToken =====> " + token);
            // 토큰을 Cookie에 설정
            connection.setRequestProperty("Cookie",token);
            LOGGER.info("[requestNiaApi]  token : "+token);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();

                LOGGER.info("[requestNiaApi]  서버 응답: " + response.toString());
            } else {
                LOGGER.info("[requestNiaApi]  서버 응답 오류. 응답 코드: " + responseCode);
            }
            connection.disconnect();
            LOGGER.info("[requestNiaApi]  disconnect");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    public void requestPostNiaLogin() {
        try {
            String url = "http://203.255.249.31:8088/ipsdn/auth/login";
            String param = "{ \"loginid\" : \"codej\", \"password\" : \"codej!@#\" }";

            String result = Utils.httpPostNiaLogin(url, param);
            JSONObject json = new JSONObject();
            json.put("result", result);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }





}

