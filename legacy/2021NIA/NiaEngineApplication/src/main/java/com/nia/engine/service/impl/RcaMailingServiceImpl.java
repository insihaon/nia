package com.nia.engine.service.impl;

import com.nia.engine.mapper.AutoProcessMapper;
import com.nia.engine.mapper.TicketMapper;
import com.nia.engine.service.RcaMailingService;
import com.nia.engine.vo.RCATicket;
import com.nia.engine.vo.aiToEngine.AiToEngineAnoVo;
import com.nia.engine.vo.aiToEngine.AiToEngineNoxVo;
import com.nia.engine.vo.selfProcess.MailAttVo;
import com.nia.engine.vo.selfProcess.MailEquipAndPortVo;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


@Service("RcaMailingService")
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
@RequiredArgsConstructor
public class RcaMailingServiceImpl implements RcaMailingService {
    private final org.apache.log4j.Logger LOGGER = Logger.getLogger(RcaMailingServiceImpl.class);
    private final JavaMailSender javaMailSender;
    @Autowired
    private AutoProcessMapper autoProcessMapper;
    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public boolean NttsendMail(AiToEngineNoxVo aiToEngineNoxVo) {
        boolean msg = false;
        HashMap<String, String> map;
        try {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            RCATicket rcaTicket = ticketMapper.selectTicket(aiToEngineNoxVo.getTicketId());
            MailEquipAndPortVo mailEquipAndPortVo = autoProcessMapper.selectRequestEquipAndPort(aiToEngineNoxVo.getTicketId());
            LOGGER.info("start sendMail()");


            String rootCauseSysnamea = mailEquipAndPortVo.getRootCauseSysnamea();
            String rootCausePorta = mailEquipAndPortVo.getRootCausePorta();
            String rootCauseSysnamez = mailEquipAndPortVo.getRootCauseSysnamez();
            String rootCausePortz = mailEquipAndPortVo.getRootCausePortz();
            String noxTicketId = aiToEngineNoxVo.getTicketId();

            if (rootCauseSysnamea == null) {
                rootCauseSysnamea = "";
            }
            if (rootCausePorta == null) {
                rootCausePorta = "";
            }
            if (rootCauseSysnamez == null) {
                rootCauseSysnamez = "";
            }
            if (rootCausePortz == null) {
                rootCausePortz = "";
            }
            if (noxTicketId == null){
                noxTicketId = "";
            }


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");
            String currentTime = dateFormat.format(new Date());
            helper.setSubject(currentTime + " 장비 조치 요청서");

            helper.setTo("codej@koren.kr");
            helper.setFrom("noc@koren.kr");
            String htmlContent ="    <h2>\n" +
                    "        &nbsp;&nbsp;&nbsp;\n" +
                    "        장애 상세내역 및 조치 요청서\n" +
                    "    </h2>\n" +
                    "    <div><br></div>\n" +
                    "<div style=\"margin-right: 20px;\">&nbsp;<a style=\"cursor: pointer;\" href=\" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+noxTicketId+"&isEmail=true&self_process_group=SO&self_process_type=N\">바로가기</a><br></div>"+
                    "    <h4>1. 발신</h4>\n" +
                    "    <div>&nbsp; &nbsp; NOC NIA ADMIN&nbsp;</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>2. AI 분석 결과 정보</h4>\n" +
                    "<h6 style=\"font-weight: 700;\">유해 트래픽</h6>\n" +
                    "    <h4>3. 수신</h4>\n" +
                    "    <div class=\"selectedManagerConfig\">&nbsp; &nbsp;CodeJ</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>4. 작업 요청 내용</h4>\n" +
                    "    <div>&nbsp;- 작업 요청 구간 : "+rootCauseSysnamea+"→"+rootCauseSysnamez+"</div>\n" +
                    "    <div>&nbsp;- 티켓 번호 : "+noxTicketId+"<br></div>\n" +
                    "    <div>&nbsp;- 티켓 타입 : NTT <br></div>\n" +
                    "    <div>&nbsp;- 발생 원인 : "+rcaTicket.getTicketRcaResultOrigDtlCode()+"<br></div>\n" +
                    "    <div>&nbsp;- 발생 시간 : "+rcaTicket.getFaultTime()+"<br></div>\n" +
                    "    <div>&nbsp;- 상세 내용 : <span class=\"recommendDetailConfig\"></span><br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>5. 장애 구간</h4>\n" +
                    "    <div>&nbsp;- 장애 구간:" +rootCauseSysnamea+"("+rootCausePorta+")"+"→"+rootCauseSysnamez+"("+rootCausePortz+")"+"<br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>6. 연관 SOP</h4>\n" +
                    "    <div>&nbsp;- 장애 구분: <span class=\"faultClassifyConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 장애 유형: <span class=\"faultTypeConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 조치 내용: <span class=\"faultDetailConfig\"></span><br></div>" ;
            helper.setText(htmlContent, true);
//            LOGGER.info("전송 url : "+" http://localhost:8080/#/nia/nia.selfProcess/?ticket_id="+noxTicketId+"&isEmail=true&self_process_group=SO&self_process_type=A");
            LOGGER.info("전송 url : "+" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+noxTicketId+"&isEmail=true&self_process_group=SO&self_process_type=N");

            LOGGER.info("메일 전송 정보: " + helper.getMimeMessage());
            javaMailSender.send(message);
            LOGGER.info("메일 전송 완료");
            msg = true;

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("메일 전송 오류" + e);
            return msg;
        }


        return msg = true;
    }

    @Override
    public boolean NttFttsendMail(AiToEngineNoxVo aiToEngineNoxVo) {
        //NTT 비장애
        boolean msg = false;
        HashMap<String, String> map;
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            map = new HashMap<>();
            MimeMessage message = javaMailSender.createMimeMessage();
            RCATicket rcaTicket = ticketMapper.selectTicket(aiToEngineNoxVo.getTicketId());
            MailEquipAndPortVo mailEquipAndPortVo = autoProcessMapper.selectRequestEquipAndPort(aiToEngineNoxVo.getTicketId());
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            map = new HashMap<>();

            double issuePercentage = aiToEngineNoxVo.getZero1Entropy()*100;
            int roundedissuePercentage = (int) Math.round(issuePercentage);
            double noIssuePercentage = (1-aiToEngineNoxVo.getZero1Entropy())*100;
            int roundednoIssuePercentage = (int) Math.round(noIssuePercentage);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");
            String currentTime = dateFormat.format(new Date());
            helper.setSubject(currentTime + " 장비 조치 요청서");

            LOGGER.info("start sendMail()");

            helper.setTo("codej@koren.kr");
            helper.setFrom("noc@koren.kr");
            String htmlContent = "    <h2>\n" +
                    "        &nbsp;&nbsp;&nbsp;\n" +
                    "        장애 상세내역 및 조치 요청서\n" +
                    "    </h2>\n" +
                    "    <div><br></div>\n" +
                    "    <div style=margin-right: 20px;>&nbsp;<a style=cursor: pointer; href=http://localhost:8080/#/nia/nia.selfProcess/?ticket_id="+aiToEngineNoxVo.getTicketId()+"&isEmail=true&self_process_group=ST&self_process_type=F>바로가기</a><br></div>"+
                    "    <h4>1. 발신</h4>\n" +
                    "    <div>&nbsp; &nbsp; NOC NIA ADMIN&nbsp;</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>2. AI 분석 결과 정보</h4>\n" +
                    "<h6 style=\"font-weight: 700;\">비장애</h6>\n" +
                    "    <div>&nbsp; &nbsp;- 장애 확률 : "+roundedissuePercentage+"%</div>\n" +
                    "    <div>&nbsp; &nbsp;- 비장애 확률 : "+roundednoIssuePercentage+"%</div>" +
                    "    <br>\n" +
                    "    <h4>3. 수신</h4>\n" +
                    "    <div class=\"selectedManagerConfig\">&nbsp; &nbsp;CodeJ</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>4. 작업 요청 내용</h4>\n" +
                    "    <div>&nbsp;- 작업 요청 구간 : "+mailEquipAndPortVo.getRootCauseSysnamea()+"→"+mailEquipAndPortVo.getRootCauseSysnamez()+"</div>\n" +
                    "    <div>&nbsp;- 티켓 번호 : "+aiToEngineNoxVo.getTicketId()+"<br></div>\n" +
                    "    <div>&nbsp;- 티켓 타입 : "+aiToEngineNoxVo.getTicketType()+"<br></div>\n" +
                    "    <div>&nbsp;- 발생 원인 : "+rcaTicket.getTicketRcaResultOrigDtlCode()+"<br></div>\n" +
                    "    <div>&nbsp;- 발생 시간 : "+rcaTicket.getFaultTime()+"<br></div>\n" +
                    "    <div>&nbsp;- 상세 내용 : <span class=\"recommendDetailConfig\"></span><br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>5. 장애 구간</h4>\n" +
                    "    <div>&nbsp;- 장애 구간:" +mailEquipAndPortVo.getRootCauseSysnamea()+"("+mailEquipAndPortVo.getRootCausePorta()+")"+"→"+mailEquipAndPortVo.getRootCauseSysnamez()+"("+mailEquipAndPortVo.getRootCausePortz()+")"+"<br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>6. 연관 SOP</h4>\n" +
                    "    <div>&nbsp;- 장애 구분: <span class=\"faultClassifyConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 장애 유형: <span class=\"faultTypeConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 조치 내용: <span class=\"faultDetailConfig\"></span><br></div>" ;
            helper.setText(htmlContent, true);
            LOGGER.info("전송 url : "+" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+aiToEngineNoxVo.getTicketId()+"&isEmail=true&self_process_group=ST&self_process_type=F");

            LOGGER.info("메일 전송 정보: " + helper.getMimeMessage());
            javaMailSender.send(message);
            LOGGER.info("메일 전송 완료");
            msg = true;

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("메일 전송 오류" + e);
            return msg;
        }


        return msg = true;
    }

    @Override
    public boolean attSendMail(AiToEngineAnoVo aiToEngineAno) {
        //ATT 장애
        boolean msg = false;
        HashMap<String, String> map;
        try {

            LOGGER.info(">>>>> Start attSendMail <<<<<");
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//           fltppsin = 26015
//fltppsout = 5540
            map = new HashMap<>();
            map.put("nodeid",aiToEngineAno.getNodeId());
            map.put("ifid",aiToEngineAno.getIfId());
            map.put("time", String.valueOf(aiToEngineAno.getInttimestamp()));
            LOGGER.info("mailing ATT2 data address >>> "+map);
            MailAttVo mailAttVo = autoProcessMapper.selectMailingAtt(map);
            RCATicket rcaTicket = ticketMapper.selectTicket(aiToEngineAno.getTicketId());
            MailEquipAndPortVo mailEquipAndPortVo = autoProcessMapper.selectRequestEquipAndPort(aiToEngineAno.getTicketId());


            String fltbpsin = null;
            String fltbpsinPredict = null;
            String fltbpsinThresholdUpper = null;
            String fltbpsinThresholdLower = null;
            boolean fltbpsinAnomaly = false;

            String fltbpsout = null;
            String fltbpsoutPredict = null;
            String fltbpsoutThresholdUpper = null;
            String fltbpsoutThresholdLower = null;
            boolean fltbpsoutAnomaly = false;

            String rootCauseSysnamea = null;
            String rootCauseSysnamez= null;
            String rootCausePorta = null;
            String rootCausePortz = null;

            if (mailAttVo == null){
                fltbpsin = "";
                fltbpsinPredict = "";
                fltbpsinThresholdUpper = "";
                fltbpsinThresholdLower = "";
                fltbpsinAnomaly = false;

                fltbpsout = "";
                fltbpsoutPredict = "";
                fltbpsoutThresholdUpper = "";
                fltbpsoutThresholdLower = "";
                fltbpsoutAnomaly = false;

                rootCauseSysnamea = mailEquipAndPortVo.getRootCauseSysnamea();
                rootCauseSysnamez= mailEquipAndPortVo.getRootCauseSysnamez();
                rootCausePorta = mailEquipAndPortVo.getRootCausePorta();
                rootCausePortz = mailEquipAndPortVo.getRootCausePortz();
            }else{
                fltbpsin = String.valueOf(mailAttVo.getFltbpsin());
                fltbpsinPredict = String.valueOf(mailAttVo.getFltbpsinPredict());
                fltbpsinThresholdUpper = String.valueOf(mailAttVo.getFltbpsinThresholdUpper());
                fltbpsinThresholdLower = String.valueOf(mailAttVo.getFltbpsinThresholdLower());
                fltbpsinAnomaly = mailAttVo.isFltbpsinAnomaly();

                fltbpsout = String.valueOf(mailAttVo.getFltbpsout());
                fltbpsoutPredict = String.valueOf(mailAttVo.getFltbpsoutPredict());
                fltbpsoutThresholdUpper = String.valueOf(mailAttVo.getFltbpsoutThresholdUpper());
                fltbpsoutThresholdLower = String.valueOf(mailAttVo.getFltbpsoutThresholdLower());
                fltbpsoutAnomaly = mailAttVo.isFltbpsoutAnomaly();

                rootCauseSysnamea = mailEquipAndPortVo.getRootCauseSysnamea();
                rootCauseSysnamez= mailEquipAndPortVo.getRootCauseSysnamez();
                rootCausePorta = mailEquipAndPortVo.getRootCausePorta();
                rootCausePortz = mailEquipAndPortVo.getRootCausePortz();

                // null 체크 후 값이 null이면 빈 문자열로 대체
                if (fltbpsin == null) {
                    fltbpsin = "";
                }
                if (fltbpsinPredict == null) {
                    fltbpsinPredict = "";
                }
                if (fltbpsinThresholdUpper == null) {
                    fltbpsinThresholdUpper = "";
                }
                if (fltbpsinThresholdLower == null) {
                    fltbpsinThresholdLower = "";
                }

                if (fltbpsout == null) {
                    fltbpsout = "";
                }
                if (fltbpsoutPredict == null) {
                    fltbpsoutPredict = "";
                }
                if (fltbpsoutThresholdUpper == null) {
                    fltbpsoutThresholdUpper = "";
                }
                if (fltbpsoutThresholdLower == null) {
                    fltbpsoutThresholdLower = "";
                }
            }






            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");
            String currentTime = dateFormat.format(new Date());
            helper.setSubject(currentTime + " 장비 조치 요청서");

            LOGGER.info("start sendMail()");

            helper.setTo("codej@koren.kr");
            helper.setFrom("noc@koren.kr");
            String htmlContent ="    <h2>\n" +
                    "        &nbsp;&nbsp;&nbsp;\n" +
                    "        장애 상세내역 및 조치 요청서\n" +
                    "    </h2>\n" +
                    "    <div><br></div>\n" +
                    "<div style=\"margin-right: 20px;\">&nbsp;<a style=\"cursor: pointer;\" href=\" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+aiToEngineAno.getTicketId()+"&isEmail=true&self_process_group=SO&self_process_type=A\">바로가기</a><br></div>"+
                    "    <h4>1. 발신</h4>\n" +
                    "    <div>&nbsp; &nbsp; NOC NIA ADMIN&nbsp;</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>2. AI 분석 결과 정보</h4>\n" +
                    "<h6 style=\"font-weight: 700;\">이상 트래픽</h6>\n" +
                    "    <span >IN</span>\n" +
                    "    <div>&nbsp;- bps : "+ fltbpsin +"</div>\n" +
                    "    <div>&nbsp;- Predict : "+ fltbpsinPredict +"</div>\n" +
                    "    <div>&nbsp;- Threshold_Upper : "+ fltbpsinThresholdUpper +"</div>\n" +
                    "    <div>&nbsp;- Threshold_Lower : "+ fltbpsinThresholdLower +"</div>\n" +
                    "    <div>&nbsp;- Anomaly : "+ fltbpsinAnomaly +"</div>\n" +
                    "    <br>\n" +
                    "    <span >OUT</span>\n" +
                    "    <div>&nbsp;- bps : "+ fltbpsout +"</div>\n" +
                    "    <div>&nbsp;- Predict : "+ fltbpsoutPredict +"</div>\n" +
                    "    <div>&nbsp;- Threshold_Upper : "+ fltbpsoutThresholdUpper +"</div>\n" +
                    "    <div>&nbsp;- Threshold_Lower : "+ fltbpsoutThresholdLower +"</div>\n" +
                    "    <div>&nbsp;- Anomaly : "+ fltbpsoutAnomaly +"</div>\n" +
                    "    <br>\n" +
                    "    <h4>3. 수신</h4>\n" +
                    "    <div class=\"selectedManagerConfig\">&nbsp; &nbsp;CodeJ</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>4. 작업 요청 내용</h4>\n" +
                    "    <div>&nbsp;- 작업 요청 구간 : "+rootCauseSysnamea+"→"+rootCauseSysnamez+"</div>\n" +
                    "    <div>&nbsp;- 티켓 번호 : "+aiToEngineAno.getTicketId()+"<br></div>\n" +
                    "    <div>&nbsp;- 티켓 타입 : ATT2 <br></div>\n" +
                    "    <div>&nbsp;- 발생 원인 : "+rcaTicket.getTicketRcaResultOrigDtlCode()+"<br></div>\n" +
                    "    <div>&nbsp;- 발생 시간 : "+rcaTicket.getFaultTime()+"<br></div>\n" +
                    "    <div>&nbsp;- 상세 내용 : <span class=\"recommendDetailConfig\"></span><br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>5. 장애 구간</h4>\n" +
                    "    <div>&nbsp;- 장애 구간:" +rootCauseSysnamea+"("+rootCausePorta+")"+"→"+rootCauseSysnamez+"("+rootCausePortz+")"+"<br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>6. 연관 SOP</h4>\n" +
                    "    <div>&nbsp;- 장애 구분: <span class=\"faultClassifyConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 장애 유형: <span class=\"faultTypeConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 조치 내용: <span class=\"faultDetailConfig\"></span><br></div>" ;
            helper.setText(htmlContent, true);
            LOGGER.info("전송 url : "+" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+aiToEngineAno.getTicketId()+"&isEmail=true&self_process_group=SO&self_process_type=A");

            LOGGER.info("메일 전송 정보: " + helper.getMimeMessage());
            javaMailSender.send(message);
            LOGGER.info("메일 전송 완료");
            msg = true;

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("메일 전송 오류" + e);
            return msg;
        }


        return msg = true;
    }

    @Override
    public boolean AttFttsendMail(AiToEngineAnoVo aiToEngineAno) {
        //ATT 비장애
        boolean msg = false;
        HashMap<String, String> map;
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            MailEquipAndPortVo mailEquipAndPortVo = autoProcessMapper.selectRequestEquipAndPort(aiToEngineAno.getTicketId());
            RCATicket rcaTicket = ticketMapper.selectTicket(aiToEngineAno.getTicketId());
            map = new HashMap<>();


            double issuePercentage = aiToEngineAno.getZero1Entropy()*100;
            int roundedissuePercentage = (int) Math.round(issuePercentage);
            double noIssuePercentage = (1-aiToEngineAno.getZero1Entropy())*100;
            int roundednoIssuePercentage = (int) Math.round(noIssuePercentage);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");
            String currentTime = dateFormat.format(new Date());
            helper.setSubject(currentTime + " 장비 조치 요청서");


            LOGGER.info("start sendMail()");

            helper.setTo("codej@koren.kr");
            helper.setFrom("noc@koren.kr");
            String htmlContent = "    <h2>\n" +
                    "        &nbsp;&nbsp;&nbsp;\n" +
                    "        장애 상세내역 및 조치 요청서\n" +
                    "    </h2>\n" +
                    "    <div><br></div>\n" +
                    "<div style=\"margin-right: 20px;\">&nbsp;<a style=\"cursor: pointer;\" href=\" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+aiToEngineAno.getTicketId()+"&isEmail=true&self_process_group=ST&self_process_type=F\">바로가기</a><br></div>"+
                    "    <h4>1. 발신</h4>\n" +
                    "    <div>&nbsp; &nbsp; $NOC NIA ADMIN&nbsp;NOC NIA ADMIN</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>2. AI 분석 결과 정보</h4>\n" +
                    "<h6 style=\"font-weight: 700;\">비장애</h6>\n" +
                    "    <div>&nbsp; &nbsp;- 장애 확률 : "+roundedissuePercentage+"%</div>\n" +
                    "    <div>&nbsp; &nbsp;- 비장애 확률 : "+roundednoIssuePercentage+"%</div>" +
                    "    <br>\n" +
                    "    <h4>3. 수신</h4>\n" +
                    "    <div class=\"selectedManagerConfig\">&nbsp; &nbsp;CodeJ</div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>4. 작업 요청 내용</h4>\n" +
                    "    <div>&nbsp;- 작업 요청 구간 : "+mailEquipAndPortVo.getRootCauseSysnamea()+"→"+mailEquipAndPortVo.getRootCauseSysnamez()+"</div>\n" +
                    "    <div>&nbsp;- 티켓 번호 : "+aiToEngineAno.getTicketId()+"<br></div>\n" +
                    "    <div>&nbsp;- 티켓 타입 : ATT2 <br></div>\n" +
                    "    <div>&nbsp;- 발생 원인 : "+rcaTicket.getTicketRcaResultOrigDtlCode()+"<br></div>\n" +
                    "    <div>&nbsp;- 발생 시간 : "+rcaTicket.getFaultTime()+"<br></div>\n" +
                    "    <div>&nbsp;- 상세 내용 : <span class=\"recommendDetailConfig\"></span><br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>5. 장애 구간</h4>\n" +
                    "    <div>&nbsp;- 장애 구간:" +mailEquipAndPortVo.getRootCauseSysnamea()+"("+mailEquipAndPortVo.getRootCausePorta()+")"+"→"+mailEquipAndPortVo.getRootCauseSysnamez()+"("+mailEquipAndPortVo.getRootCausePortz()+")"+"<br></div>\n" +
                    "    <div><br></div>\n" +
                    "    <h4>6. 연관 SOP</h4>\n" +
                    "    <div>&nbsp;- 장애 구분: <span class=\"faultClassifyConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 장애 유형: <span class=\"faultTypeConfig\"></span><br></div>\n" +
                    "    <div>&nbsp;- 조치 내용: <span class=\"faultDetailConfig\"></span><br></div>" ;

            LOGGER.info("전송 url : "+" http://116.89.191.47:8080/#/nia/nia.selfProcess/?ticket_id="+aiToEngineAno.getTicketId()+"&isEmail=true&self_process_group=ST&self_process_type=F");

            helper.setText(htmlContent, true);

            LOGGER.info("메일 전송 정보: " + helper.getMimeMessage());
            javaMailSender.send(message);
            LOGGER.info("메일 전송 완료");
            msg = true;

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("메일 전송 오류" + e);
            return msg;
        }


        return msg = true;
    }


}
