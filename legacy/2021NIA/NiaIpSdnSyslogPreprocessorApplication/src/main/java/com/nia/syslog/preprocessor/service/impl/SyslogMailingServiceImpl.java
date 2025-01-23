package com.nia.syslog.preprocessor.service.impl;

import com.nia.syslog.preprocessor.mapper.SelfProcessSyslogMapper;
import com.nia.syslog.preprocessor.service.SyslogMailingService;
import com.nia.syslog.preprocessor.vo.alarm.SysLogAlarmVo;
import com.nia.syslog.preprocessor.vo.selfProcess.SyslogAlarmVo;
import com.nia.syslog.preprocessor.vo.selfProcess.SyslogSopVo;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service("SyslogMailingService")
@RequiredArgsConstructor
public class SyslogMailingServiceImpl implements SyslogMailingService {
    private final Logger LOGGER = Logger.getLogger(SyslogMailingServiceImpl.class);



    private final JavaMailSender javaMailSender;

    @Autowired
    private SelfProcessSyslogMapper selfProcessSyslogMapper;


    @Override
    public boolean syslogSendMail(SysLogAlarmVo sysLogAlarmVo) {
        //ATT 장애
        boolean msg = false;
        HashMap<String, String> map;
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            SyslogAlarmVo syslogAlarmVo = selfProcessSyslogMapper.selectSyslogAlarmMst(sysLogAlarmVo.getAlarmno());
            map = new HashMap<>();
            SyslogSopVo syslogSopVo = selfProcessSyslogMapper.selectSyslogSop(syslogAlarmVo.getAlarmno());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");
            String currentTime = dateFormat.format(new Date());
            helper.setSubject(currentTime + " 장비 조치 요청서");

            LOGGER.info("start sendMail()");

            helper.setTo("codej@koren.kr");
            helper.setFrom("noc@koren.kr");
            String htmlContent ="<h2>\n" +
                    "        &nbsp;&nbsp;&nbsp;\n" +
                    "        장애 상세내역 및 조치 요청서\n" +
                    "    </h2>\n" +
                    "    <div><br></div>\n" +
                    "<div style=\"margin-right: 20px;\">&nbsp;<a style=\"cursor: pointer;\" href=\" http://116.89.191.47:8080/#/nia/nia.selfProcess/?alarmno="+syslogAlarmVo.getAlarmno()+"&isEmail=true&self_process_group=ST&self_process_type=S\">바로가기</a><br></div>" +
                    "        <h4>1. 발신</h4>\n" +
                    "        <div>&nbsp; &nbsp; NOC NIA ADMIN&nbsp;</div>\n" +
                    "        <div><br></div>\n" +
                    "        <h4>2. 수신</h4>\n" +
                    "        <div class=\"selectedManagerConfig\">&nbsp; &nbsp;CodeJ</div>\n" +
                    "        <div><br></div>\n" +
                    "        <h4>3. 작업 요청 내용</h4>\n" +
                    "        <div>&nbsp;- 발생 시간 : "+syslogAlarmVo.getAlarmtime()+"<br></div>\n" +
                    "        <div>&nbsp;- 알람 번호 : "+syslogAlarmVo.getAlarmno()+"<br></div>\n" +
                    "        <div>&nbsp;- 장비명 : "+syslogAlarmVo.getNodeNm()+"</div>\n" +
                    "        <div>&nbsp;- 장비 번호 : "+syslogAlarmVo.getNodeNum()+"<br></div>\n" +
                    "        <div>&nbsp;- 인터페이스 : "+syslogAlarmVo.getAlarmloc()+"</div>\n" +
                    "        <div>&nbsp;- 알람 메시지 : "+syslogAlarmVo.getAlarmmsg()+"<br></div>\n" +
                    "        <div>&nbsp;- 원본 메시지 : "+syslogAlarmVo.getEtc()+"<br></div>\n" +
                    "        <div>&nbsp;- 상세 내용 : <span class=\"recommendDetailConfig\"></span><br></div>\n" +
                    "        <div><br></div>\n" +
                    "        <h4>4. 장애 구역</h4>\n" +
                    "        <div>&nbsp;- 장애 구역: "+syslogAlarmVo.getNodeNm()+"("+syslogAlarmVo.getAlarmloc()+")"+"<br></div>\n" +
                    "        <div><br></div>\n" +
                    "        <h4>5. 연관 SOP</h4>\n" +
                    "        <div>&nbsp;- 장애 구분: <span class=\"faultClassifyConfig\">&nbsp;"+syslogSopVo.getFaultClassify()+"</span><br></div>\n" +
                    "        <div>&nbsp;- 장애 유형: <span class=\"faultTypeConfig\">&nbsp;"+syslogSopVo.getFaultType()+"</span><br></div>\n" +
                    "        <div>&nbsp;- 조치 내용: <span class=\"faultDetailConfig\">&nbsp;"+syslogSopVo.getFaultDetailContent()+"</span><br></div>\n" +
                    "    <div><br></div>";

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
