package com.codej.nia.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import com.codej.base.dto.AppDto;
import com.codej.base.utils.JsonUtil;
import com.codej.base.dto.response.ResultResponse;
import com.codej.nia.controller.NiaApiController;
import com.codej.nia.mq.handler.NiaUiToEnginePublisher;
import com.codej.web.service.MainService;
import com.codej.web.service.ResponseService;
import com.codej.base.utils.CryptUtil;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

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
    @Lazy
    private NiaUiToEnginePublisher niaUiToEnginePublisher;

    @Autowired
    private AppDto appDto;

    @Value("${myconf.mail.host:smtp.gmail.com}") // 구글 메일 서버 호스트 이름
    private String host;
    @Value("${myconf.mail.port:587}")
    private Integer port;
    @Value("${myconf.mail.sender:codej@koren.kr}") // 보내는 사람의 이메일 주소
    private String from;
    @Value("${myconf.mail.password:zgpi wyws mnko mokj}") // 보내는 사람의 이메일 계정 비밀번호
    private String password;


    // public List<ResultMap> SELECT_API_LIST(HashMap<String, Object> param) throws Exception {
    //     param.remove("TOTAL_COUNT");
    //     List<ResultMap> resultList = niaMapper.SELECT_API_LIST(param);
    //     return resultList;
    // }

    public ResultResponse<?> mailProcessing(Map<String, Object> param) throws Exception {
        try {
            Map<String, Object> mailInfo = (Map<String, Object>)param.get("mail");
            Boolean resMail = sendEmail(mailInfo);
            // 결과가 성공일 경우에 ticket정보 update 수행
            if(resMail) {
                Map<String, Object> ticketInfo = (Map<String, Object>)param.get("ticketInfo");
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

        if(host == null) {
            return false;
        }
        String receiverUsers = mailInfo.get("receiverUser").toString(); // 받는 사람의 이메일 주소
        String subject = mailInfo.get("subject").toString();
        String content = mailInfo.get("content").toString();

        // SMTP 프로토콜 설정
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", appDto.getAuthUse());
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        // 보내는 사람 계정 정보 설정
        Session session = Session.getInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(from, password);
            }
        });
        try {
            // 메일 내용 작성
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
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
    
}
