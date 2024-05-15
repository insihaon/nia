package com.codej.nia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.model.ResultMap;
import com.codej.base.dto.response.ResultResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.base.exception.CHttpRelayServiceFail;
import com.codej.base.utils.CommonUtil;
import com.codej.base.utils.HttpUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.nia.controller.NiaApiController;
import com.codej.nia.mapper.db2nd.NiaMapper;
import com.codej.nia.mq.handler.NiaUiToEnginePublisher;
import com.codej.nia.properties.ApiServerProperites;
import com.codej.nia.utils.SSHUtil;
import com.codej.web.service.MainService;
import com.codej.web.service.ResponseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class NiaService extends MainService {

    @Autowired
    @Lazy
    protected NiaApiController NiaApiController;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private NiaMapper niaMapper;

    @Autowired
    @Lazy
    private NiaUiToEnginePublisher niaUiToEnginePublisher;

    @Autowired
    private ApiServerProperites apiServerProperites;

    @Autowired
    private AppDto appDto;

    public ResultResponse<?> mailProcessing(Map<String, Object> param) throws Exception {
        try {
            Map<String, Object> mailInfo = (Map<String, Object>) param.get("mail");
            Boolean resMail = sendEmail(mailInfo);
            // 결과가 성공일 경우에 ticket정보 update 수행
            if (resMail) {
                Map<String, Object> ticketInfo = (Map<String, Object>) param.get("ticketInfo");
                send(ticketInfo);
            } else {
                return responseService.createFailResponse();
            }
            return responseService.createSuccessResponse();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return responseService.createFailResponse();
    }

    private Boolean sendEmail(Map<String, Object> mailInfo) throws Exception {

        if (apiServerProperites.getMailHost() == null) {
            return false;
        }
        String receiverUsers = mailInfo.get("receiverUser").toString(); // 받는 사람의 이메일 주소
        String subject = mailInfo.get("subject").toString();
        String content = mailInfo.get("content").toString();

        // SMTP 프로토콜 설정
        Properties props = new Properties();
        props.put("mail.smtp.host", apiServerProperites.getMailHost());
        props.put("mail.smtp.port", apiServerProperites.getMailPort());
        props.put("mail.smtp.auth", appDto.getAuthUse());
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        // 보내는 사람 계정 정보 설정
        Session session = Session.getInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(
                        apiServerProperites.getFrom(), apiServerProperites.getMailPassword());
            }
        });
        try {
            // 메일 내용 작성
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(apiServerProperites.getFrom()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverUsers));
            msg.setSubject(subject);
            msg.setContent(content, "text/html;charset=euc-kr");

            // 메일 보내기
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    public void send(Map<String, Object> ticketInfo) throws Exception {
        try {
            // JSONObject jsonObj = JsonUtil.convertMapToJson(ticketInfo);
            niaUiToEnginePublisher.sendMessage(ticketInfo);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public Map<String, Object> getIpsdnToken() {
        String url = CommonUtil.format("{}/ipsdn/auth/login", apiServerProperites.getIpsdnUrl());
        // String param = "{ \"loginid\" : \"codej\", \"password\" : \"codej!@#\" }";

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("loginid", appDto.getIpsdnId());
        param.put("password", appDto.getIpsdnPass());
        try {
            ResponseEntity<?> responseEntity = HttpUtil.post(url, param);

            String body = (String) responseEntity.getBody();
            Map<String, Object> resMap = JsonUtil.convertJsonToMap(body);

            return (Map<String, Object>) resMap.get("data");
        } catch (Exception e) {
            log.error(e.toString());
            throw new CHttpRelayServiceFail(e);
        }
    }

    public SingleResponse<Object> ipsdnRequest(HttpServletRequest request, Map<String, Object> map)
            throws Exception {

        String servicePath = (String) map.get("servicePath");
        String requestType = (String) map.get("requestType");
        if (servicePath == null) {
            log.error("[NiaService] servicePath is null");
            throw new CHttpRelayServiceFail("servicePath is null");
        }
        if (requestType == null) {
            log.error("[NiaService] requestType is null");
            throw new CHttpRelayServiceFail("requestType is null");
        }
        String param = (String) map.get("param");

        String url = CommonUtil.format("{}/ipsdn/services/{}/?{}", apiServerProperites.getIpsdnUrl(),
                servicePath, param);

        // http: //
        // 203.255.249.31:8088/ipsdn/services/config/interfaces/shutdown?nodename=daejeon-7712&ifname=ce3/1
        log.debug("<<< Request url={}", url);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json;charset=UTF-8");
        headers.put("Cookie", String.format("accessToken=%s", request.getHeader("X-AUTH-IP-TOKEN")));

        try {
            String responseBody;
            if (requestType.equals("get")) {
                responseBody = (String) HttpUtil.get(url, headers).getBody();
            } else {
                responseBody = (String) HttpUtil.post(url, map, headers).getBody();
            }
            Map<String, Object> res = JsonUtil.convertJsonToMap(responseBody);
            return responseService.createSingleResponse(res);
        } catch (Exception e) {
            log.error(e.toString());
            throw new CHttpRelayServiceFail(e);
        }
    }

    public SingleResponse<Object> ping(HashMap<String, Object> param) {
        try {
            String fileName = SSHUtil.ping(appDto, param);
            return responseService.createSingleResponse(fileName);
            // return responseService.createSuccessResponse();
        } catch (Exception e) {
            log.error(e.toString());
            return responseService.createSingleResponse(false);
        }
    }

    public List<ResultMap> selectIpsdnAlarmList(HashMap<String, Object> map) {
        return niaMapper.SELECT_IP_ALARM_LIST(map);
    }

    public List<ResultMap> selectTransmissionAlarmList(HashMap<String, Object> map) {
        return niaMapper.SELECT_TRANSMISSION_ALARM_LIST(map);
    }

    public List<ResultMap> selectSystemMonitoringCur() {
        return niaMapper.SELECT_SYSTEM_MONITORING_CURRENT();
    }

    public int insertProfileList(HashMap<String, Object> param) throws Exception {
        int result = -1;
        int result2 = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {   
            result = niaMapper.INSERT_PROFILE_LIST(param);

            List<ResultMap> profile_num = niaMapper.SELECT_MAX_PROFILE_NUM(null);

            // 등록시 추가한 노드명 리스트(tableData) INSERT
            ArrayList<HashMap<String, Object>> tableData = (ArrayList<HashMap<String, Object>>) param.get("tableData");
            for (HashMap<String, Object> data : tableData) {
                String node_id = (String) data.get("name");
                ResultMap maxProfile = profile_num.get(0);
                String maxProfileNum = String.valueOf(maxProfile.get("max_profile_num"));
                param.put("profile_num", maxProfileNum);
                param.put("node_id", node_id);
    
                result2 = niaMapper.INSERT_PROFILE_NODE_NAME_LIST(param);
            }

            if (result > 0 && result2 > 0) {
                transactionManager.commit(txStatus);
            } else {
                throw new Exception(String.format("Fail insertProfileList, %s", param.toString()));
            }
    
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw e;
        }
        return result;  
    }

    public int deleteProfileList(HashMap<String, Object> param) throws Exception {
        int result = -1;
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try { 
            // 프로파일 삭제시 profile_num에 해당하는 노드명 리스트 DELETE
            ArrayList<HashMap<String, Object>> tableData = (ArrayList<HashMap<String, Object>>) param.get("tableData");
            for (HashMap<String, Object> data : tableData) {
                String profile_num = (String) data.get("profile_num");
                int result2 = niaMapper.DELETE_PROFILE_NODE_LIST(param);
                    if(result2 < 0){
                        throw new Exception(String.format("Failed to DELETE_PROFILE_NODE_LIST: %s", profile_num));
                    }
            }
            result = niaMapper.DELETE_PROFILE_LIST(param);

            if (result > 0) {
                transactionManager.commit(txStatus);
            } else {
                throw new Exception(String.format("Fail deleteProfileList, %s", param.toString()));
            }
    
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            log.error(e.toString());
            throw e;
        }
        return result;
    }
    
    
}
